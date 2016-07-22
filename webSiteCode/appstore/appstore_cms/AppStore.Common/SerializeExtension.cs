using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Text.RegularExpressions;
using System.Xml;
using System.Xml.Serialization;

namespace AppStore.Common
{
    /// <summary>
    /// JSON序列化、XML序列化
    /// </summary>
    public static class SerializeExtension
    {

        #region 私有方法

        /// <summary>
        /// 将Json序列化的时间由/Date(1294499956278+0800)转为字符串
        /// </summary>
        private static string ConvertJsonDateToDateString(Match m)
        {
            string result = string.Empty;
            DateTime dt = new DateTime(1970, 1, 1);
            dt = dt.AddMilliseconds(long.Parse(m.Groups[1].Value));
            dt = dt.ToLocalTime();
            result = dt.ToString("yyyy-MM-dd HH:mm:ss");
            return result;
        }

        /// <summary>
        /// 将时间字符串转为Json时间
        /// </summary>
        private static string ConvertDateStringToJsonDate(Match m)
        {
            string result = string.Empty;
            DateTime dt = DateTime.Parse(m.Groups[0].Value);
            dt = dt.ToUniversalTime();
            TimeSpan ts = dt - DateTime.Parse("1970-01-01");
            result = string.Format("\\/Date({0}+0800)\\/", ts.TotalMilliseconds);
            return result;
        }


        #endregion

        #region 公共方法

        #region 序列化JSON

        /// <summary>
        /// 序列化Json
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="target"></param>
        /// <returns></returns>
        public static string JsonSerialize<T>(this object target)
        {
            T result = (T)target;

            DataContractJsonSerializer json = new DataContractJsonSerializer(result.GetType());

            using (MemoryStream stream = new MemoryStream())
            {
                json.WriteObject(stream, result);

                //替换Json的Date字符串
                string p = @"\\/Date\((\d+)\+\d+\)\\/";
                MatchEvaluator matchEvaluator = new MatchEvaluator(ConvertJsonDateToDateString);
                Regex reg = new Regex(p);

                string JsonString = Encoding.UTF8.GetString(stream.ToArray());
                JsonString = reg.Replace(JsonString, matchEvaluator);

                return JsonString;
            }
        }


        /// <summary>
        /// 返序列化Json
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="target"></param>
        /// <returns></returns>
        public static T JsonDeserialize<T>(this string target)
        {
            //将"yyyy-MM-dd HH:mm:ss"格式的字符串转为"\/Date(1294499956278+0800)\/"格式
            string p = @"\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}:\d{2}";
            MatchEvaluator matchEvaluator = new MatchEvaluator(ConvertDateStringToJsonDate);
            Regex reg = new Regex(p);
            target = reg.Replace(target, matchEvaluator);

            using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(target)))
            {
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
                return (T)serializer.ReadObject(ms);
            }
        }


        #endregion

        #region 序列化XML

        /// <summary>
        /// 将一个对象序列化成Xml格式的字符串
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="target"></param>
        /// <returns></returns>
        public static string XmlSerialize<T>(this T target)
        {
            string xmlString = string.Empty;

            XmlSerializer xmlSerializer = new XmlSerializer(typeof(T));

            using (MemoryStream objMemoryStream = new MemoryStream())
            {
                XmlSerializerNamespaces nc = new XmlSerializerNamespaces();
                nc.Add("", "");

                xmlSerializer.Serialize(objMemoryStream, target, nc);

                xmlString = Encoding.UTF8.GetString(objMemoryStream.ToArray());
            }

            return xmlString;
        }


        /// <summary>
        /// 将字符串格式的XML反序列化成一个对象
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="target"></param>
        /// <returns></returns>
        public static T XmlDeserialize<T>(this string target)
        {
            T result = default(T);

            XmlSerializer xmlSerializer = new XmlSerializer(typeof(T));

            using (Stream objStream = new MemoryStream(Encoding.UTF8.GetBytes(target)))
            {
                using (XmlReader xmlReader = XmlReader.Create(objStream))
                {
                    Object obj = xmlSerializer.Deserialize(xmlReader);

                    result = (T)obj;
                }
            }
            return result;
        }

        #endregion

        #region 二进制序列化

        /// <summary>
        /// 二进制序列化
        /// </summary>
        /// <typeparam name="T">数据类型</typeparam>
        /// <param name="target">对象</param>
        /// <returns>二进制文本</returns>
        public static string ToBinary<T>(this object target)
        {
            StringBuilder result = new StringBuilder();

            {
                BinaryFormatter objBinaryFormatter = new BinaryFormatter();

                using (MemoryStream objMemoryStream = new MemoryStream())
                {
                    objBinaryFormatter.Serialize(objMemoryStream, target);

                    objMemoryStream.Position = 0;

                    byte[] bytes = objMemoryStream.ToArray();

                    foreach (byte bt in bytes)
                    {
                        result.Append(string.Format("{0:X2}", bt));
                    }
                }
            }

            return result.ToString();
        }


        #endregion

        #endregion

    }
}
