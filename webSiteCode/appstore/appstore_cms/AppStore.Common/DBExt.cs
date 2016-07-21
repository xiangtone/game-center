using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Configuration;
using System.Globalization;
using System.Data;
using System.Reflection;


namespace AppStore.Common
{
    public static class Extensions
    {
        #region dataReader 转换为实体类
        /// <summary>
        /// DataReader 转泛型
        /// </summary>
        /// <typeparam name="T">传入的实体类</typeparam>
        /// <param name="objReader">DataReader对象</param>
        /// <returns></returns>
        public static List<T> ReaderToList<T>(this IDataReader objReader) where T : class
        {
            if (objReader != null && !objReader.IsClosed)
            {
                List<T> list = new List<T>();

                //遍历DataReader对象
                while (objReader.Read())
                {
                    T model = _ReaderToModel<T>(objReader);
                    list.Add(model);
                }
                return list;
            }
            else
                return null;
        }

        /// <summary>
        /// DataReader转模型
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="objReader"></param>
        /// <returns></returns>
        public static T ReaderToModel<T>(this IDataReader objReader) where T : class
        {
            if (objReader != null && !objReader.IsClosed)
            {

                if (objReader.Read())
                {
                    return _ReaderToModel<T>(objReader);
                }

                return default(T);
            }
            else
            {
                return null;
            }
        }

        private static T _ReaderToModel<T>(IDataReader objReader) where T : class
        {
            Type modelType = typeof(T);
            int count = objReader.FieldCount;
            T model = Activator.CreateInstance<T>();
            for (int i = 0; i < count; i++)
            {
                if (!IsNullOrDBNull(objReader[i]))
                {
                    PropertyInfo pi = modelType.GetProperty(objReader.GetName(i), BindingFlags.GetProperty | BindingFlags.Public | BindingFlags.Instance | BindingFlags.IgnoreCase);
                    if (pi != null)
                    {
                        pi.SetValue(model, CheckType(objReader[i], pi.PropertyType), null);
                    }
                }
            }
            return model;
        }

        /// <summary>
        /// 对可空类型进行判断转换(*要不然会报错)
        /// </summary>
        /// <param name="value">DataReader字段的值</param>
        /// <param name="conversionType">该字段的类型</param>
        /// <returns></returns>
        private static object CheckType(object value, Type conversionType)
        {
            if (conversionType.IsGenericType && conversionType.GetGenericTypeDefinition().Equals(typeof(Nullable<>)))
            {
                if (value == null)
                    return null;
                System.ComponentModel.NullableConverter nullableConverter = new System.ComponentModel.NullableConverter(conversionType);
                conversionType = nullableConverter.UnderlyingType;
            }
            return Convert.ChangeType(value, conversionType);
        }

        /// <summary>
        /// 判断指定对象是否是有效值
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        private static bool IsNullOrDBNull(object obj)
        {
            return (obj == null || (obj is DBNull)) ? true : false;
        }


        /// <summary>
        /// 获取AppSettings
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="key"></param>
        /// <param name="defaultValue"></param>
        /// <returns></returns>
        public static T AppSettings<T>(string key, T defaultValue = default(T))
        {
            T result = default(T);

            if (!string.IsNullOrEmpty(key))
            {
                try
                {
                    result = (T)System.Convert.ChangeType(ConfigurationManager.AppSettings[key], typeof(T), CultureInfo.InvariantCulture);
                }
                catch
                {
                    return defaultValue;
                }
            }

            return result;
        }

        #endregion
    }
}
