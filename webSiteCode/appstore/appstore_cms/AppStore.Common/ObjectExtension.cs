using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;


namespace AppStore.Common
{
    /// <summary>
    /// ObjectExtension
    /// </summary>
    [Serializable]
    public static class ObjectExtension
    {
        #region Common

        /// <summary>
        /// 通用的转换方法
        /// </summary>
        /// <typeparam name="T">数据类型</typeparam>
        /// <param name="target">要转换的目标</param>
        /// <param name="defaultValue">默认值</param>
        /// <returns>成功返回转换结果，失败返回默认值</returns>
        public static T Convert<T>(this object target, T defaultValue = default(T))
        {
            T result = defaultValue;

            if (target != null)
            {
                try
                {
                    result = (T)System.Convert.ChangeType(target, typeof(T), CultureInfo.InvariantCulture);
                }
                catch
                {
                    return defaultValue;
                }
            }
            return result;
        }

        /// <summary>
        /// 克隆对象
        /// </summary>
        /// <typeparam name="T">数据类型</typeparam>
        /// <param name="target">对象</param>
        /// <returns>克隆后的对象</returns>
        public static T CloneObject<T>(this object target)
        {
            T result = default(T);

            using (MemoryStream objMemoryStream = new MemoryStream())
            {
                BinaryFormatter objBinaryFormatter = new BinaryFormatter
                (
                    null,
                    new StreamingContext(StreamingContextStates.Clone)
                );

                objBinaryFormatter.Serialize(objMemoryStream, target);

                objMemoryStream.Seek(0, SeekOrigin.Begin);

                result = (T)objBinaryFormatter.Deserialize(objMemoryStream);
            }

            return result;
        }



        /// <summary>
        /// 判断对象是否为空
        /// </summary>
        /// <typeparam name="T">数据类型</typeparam>
        /// <param name="target">对象</param>
        /// <returns>true->空,false->非空</returns>
        public static bool IsNullOrEmpty<T>(this object target)
        {
            bool result = false;

            if (target == null)
            {
                result = true;
            }

            if (target.GetType() == typeof(string))
            {
                if ((target as string).IsNullOrEmpty())
                {
                    result = true;
                }
            }

            if (target.GetType() == typeof(DBNull))
            {
                result = true;
            }

            if (target.GetType() == typeof(Nullable))
            {
                result = true;
            }

            return result;
        }

        public static DateTime ToDateTime(this Object obj, string timeFormat)
        {
            DateTime v = new DateTime(1900, 01, 01);
            if (!(obj == DBNull.Value || obj == null))
            {
                System.Globalization.CultureInfo cultureInfo = new System.Globalization.CultureInfo("en-US");
                // string format = "dd/MMM/yyyy:HH:mm:ss";
                // yyyy-MM-ddTHH:mm:ss.ffffffzzz
                DateTime.TryParseExact(obj.ToString(), timeFormat, cultureInfo, System.Globalization.DateTimeStyles.None, out v);
            }
            return v;
        }

        #endregion

        #region 获取属性值
        /// <summary>
        /// 获取属性值
        /// </summary>
        /// <param name="target">对象</param>
        /// <param name="propertyName">属性名</param>
        /// <returns>属性值</returns>
        public static object GetPropertyValue(this object target, string propertyName)
        {
            object result = null;

            if (target != null)
            {
                result = target.GetType().GetProperty(propertyName).GetValue(target, null);
            }

            return result;
        }

        /// <summary>
        /// 获取属性值
        /// </summary>
        /// <typeparam name="T">数据类型</typeparam>
        /// <param name="target">对象</param>
        /// <param name="propertyName">属性名</param>
        /// <returns>属性值</returns>
        public static T GetPropertyValue<T>(this object target, string propertyName)
        {
            return (T)GetPropertyValue(target, propertyName);
        }
        #endregion

        #region 判断元素是否在某个范围内
        /// <summary>
        /// 判断元素是否在某个范围内
        /// </summary>
        /// <param name="target">对象</param>
        /// <param name="min">最小值</param>
        /// <param name="max">最大值</param>
        /// <returns>true->在范围之内,false->不在范围之内</returns>
        public static bool InRange(this IComparable target, object min, object max)
        {
            return target.CompareTo(min) >= 0 && target.CompareTo(max) <= 0;
        }

        /// <summary>
        /// 判断元素是否在某个范围内
        /// </summary>
        /// <typeparam name="T">数据类型</typeparam>
        /// <param name="target">对象</param>
        /// <param name="min">最小值</param>
        /// <param name="max">最大值</param>
        /// <returns>true->在范围之内,false->不在范围之内</returns>
        public static bool InRange<T>(this IComparable<T> target, T min, T max)
        {
            return target.CompareTo(min) >= 0 && target.CompareTo(max) <= 0;
        }
        #endregion
    }
}
