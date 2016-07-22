using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Configuration;
using System.Web.Security;

namespace AppStore.Common
{
    /// <summary>
    /// StringExtension
    /// </summary>
    [Serializable]
    public static class StringExtension
    {
        #region Public Property
        public static string T1 = "    ";
        public static string T2 = "        ";
        public static string T3 = "            ";
        public static string T4 = "                ";
        public static string NewLine1 = Environment.NewLine;
        public static string NewLine2 = Environment.NewLine + Environment.NewLine;
        #endregion

        #region 类型转换

        #region Int
        /// <summary>
        /// 是否是Int
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>true->是,false->否</returns>
        public static bool IsInt(this string target)
        {
            int temp = default(int);

            return int.TryParse(target, out temp);
        }

        /// <summary>
        /// 转Int
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>Int</returns>
        public static int ToInt(this string target)
        {
            int result = default(int);

            int.TryParse(target, out result);

            return result;
        }
        #endregion

        #region Decimal
        /// <summary>
        /// 是否是Decimal
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>true->是,false->否</returns>
        public static bool IsDecimal(this string target)
        {
            decimal temp = default(decimal);

            return decimal.TryParse(target, out temp);
        }

        /// <summary>
        /// 转Decimal
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>Decimal</returns>
        public static decimal ToDecimal(this string target)
        {
            decimal result = default(decimal);

            decimal.TryParse(target, out result);

            return result;
        }
        #endregion

        #region Bool
        /// <summary>
        /// 是否是Bool
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>true->是,false->否</returns>
        public static bool IsBool(this string target)
        {
            bool temp = default(bool);

            return bool.TryParse(target, out temp);
        }

        /// <summary>
        /// 转Bool
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>Bool</returns>
        public static bool ToBool(this string target)
        {
            bool result = default(bool);

            bool.TryParse(target, out result);

            return result;
        }
        #endregion

        #region DateTime
        /// <summary>
        /// 是否是DateTime
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>true->是,false->否</returns>
        public static bool IsDateTime(this string target)
        {
            DateTime temp = default(DateTime);

            return DateTime.TryParse(target, out temp);
        }

        /// <summary>
        /// 转DateTime
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>DateTime</returns>
        public static DateTime ToDateTime(this string target)
        {
            DateTime result = default(DateTime);

            DateTime.TryParse(target, out result);

            return result;
        }

        /// <summary>
        /// 转DateTime
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>DateTime</returns>
        public static DateTime ToDateTimeWithStart(this string target)
        {
            DateTime temp = target.ToDateTime();

            return new DateTime(temp.Year, temp.Month, temp.Day, 0, 0, 0);
        }

        /// <summary>
        /// 转DateTime
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>DateTime</returns>
        public static DateTime ToDateTimeWithEnd(this string target)
        {
            DateTime temp = target.ToDateTime();

            return new DateTime(temp.Year, temp.Month, temp.Day, 23, 59, 59);
        }
        #endregion

        #endregion

        #region 判断空字符串
        /// <summary>
        /// 判断是否是空字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>true->空字符串,false->非空字符串</returns>
        public static bool IsNullOrEmpty(this string target)
        {
            return String.IsNullOrEmpty(target);
        }

        /// <summary>
        /// 判断是否是非空字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>true->非空字符串,false->空字符串</returns>
        public static bool IsNotNullOrEmpty(this string target)
        {
            return !String.IsNullOrEmpty(target);
        }
        #endregion

        #region 匹配正则表达式
        /// <summary>
        /// 是否匹配正则表达式
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="pattern">正则表达式</param>
        /// <returns>true->匹配,false-不匹配</returns>
        public static bool IsMatch(this string target, string pattern)
        {
            bool result = false;

            if (target != null)
            {
                result = Regex.IsMatch(target, pattern);
            }

            return result;
        }

        /// <summary>
        /// 获得匹配正则表达式的字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="pattern">正则表达式</param>
        /// <returns>匹配正则表达式的字符串</returns>
        public static string GetMatch(this string target, string pattern)
        {
            string result = null;

            if (target != null)
            {
                result = Regex.Match(target, pattern).Value;
            }

            return result;
        }
        #endregion

        #region 全角半角转换
        /// <summary>
        /// 转全角
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>全角字符串</returns>
        public static string ToSBC(this string target)
        {
            char[] c = target.ToCharArray();

            for (int i = 0; i < c.Length; i++)
            {
                if (c[i] == 32)
                {
                    c[i] = (char)12288;

                    continue;
                }

                if (c[i] < 127)
                {
                    c[i] = (char)(c[i] + 65248);
                }
            }

            return new string(c);
        }

        /// <summary>
        /// 转半角
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>半角字符串</returns>
        public static string ToDBC(this string target)
        {
            char[] c = target.ToCharArray();

            for (int i = 0; i < c.Length; i++)
            {
                if (c[i] == 12288)
                {
                    c[i] = (char)32;

                    continue;
                }

                if (c[i] > 65280 && c[i] < 65375)
                {
                    c[i] = (char)(c[i] - 65248);
                }
            }

            return new string(c);
        }
        #endregion

        #region 格式化字符串
        /// <summary>
        /// 使用指定的参数格式化字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="arg0">参数1</param>
        /// <returns>格式化后的字符串</returns>
        public static string FormatWith(this string target, object arg0)
        {
            return String.Format(target, arg0);
        }

        /// <summary>
        /// 使用指定的参数格式化字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="arg0">参数1</param>
        /// <param name="arg1">参数2</param>
        /// <returns>格式化后的字符串</returns>
        public static string FormatWith(this string target, object arg0, object arg1)
        {
            return String.Format(target, arg0, arg1);
        }

        /// <summary>
        /// 使用指定的参数格式化字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="arg0">参数1</param>
        /// <param name="arg1">参数2</param>
        /// <param name="arg2">参数3</param>
        /// <returns>格式化后的字符串</returns>
        public static string FormatWith(this string target, object arg0, object arg1, object arg2)
        {
            return String.Format(target, arg0, arg1, arg2);
        }

        /// <summary>
        /// 使用指定的参数集合格式化字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="args">参数集合</param>
        /// <returns>格式化后的字符串</returns>
        public static string FormatWith(this string target, params object[] args)
        {
            return String.Format(target, args);
        }

        /// <summary>
        /// 使用指定的格式化方式修饰字符串        
        /// </summary>         
        /// <param name="target">字符串</param>  
        /// <param name="formatter">格式化器,包含有且仅有一个{0}字符位置</param>        
        /// <returns>格式化后的字符串</returns>
        public static string SurroundWith(this string target, string formatter)
        {
            return string.Format(formatter, target);
        }
        #endregion

        #region 截取字符串
        /// <summary>
        /// 截取字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="length">截取长度</param>
        /// <returns>截取后的字符串</returns>
        public static String SubFullString(this string target, int length)
        {
            string result = null;

            if (!String.IsNullOrEmpty(target))
            {
                byte[] bytes = Encoding.Unicode.GetBytes(target);

                int n = 0;
                int i = 0;

                for (; i < bytes.GetLength(0) && n < length; i++)
                {
                    if (i % 2 == 0)
                    {
                        n++;
                    }

                    else
                    {
                        if (bytes[i] > 0)
                        {
                            n++;
                        }
                    }
                }

                if (i % 2 == 1)
                {
                    if (bytes[i] > 0)
                    {
                        i = i - 1;
                    }

                    else
                    {
                        i = i + 1;
                    }
                }

                result = Encoding.Unicode.GetString(bytes, 0, i);
            }

            return result;
        }

        /// <summary>
        /// 截取字符串
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="length">截取长度</param>
        /// <param name="suffix">后缀</param>
        /// <returns>截取后的字符串</returns>
        public static String SubFullString(this string target, int length, string suffix)
        {
            return SubFullString(target, length) + suffix;
        }
        #endregion

        #region SHA1加密
        /// <summary>
        /// SHA1加密
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>加密后的字符串</returns>
        public static string SHA1Encrpty(this string target)
        {
            return FormsAuthentication.HashPasswordForStoringInConfigFile(target, FormsAuthPasswordFormat.SHA1.ToString());
        }
        #endregion

        #region Base64编码与解码
        /// <summary>
        /// Base64编码(UTF8)
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>编码后的字符串</returns>
        public static string Base64Encode(this string target)
        {
            return Convert.ToBase64String(Encoding.UTF8.GetBytes(target));
        }

        /// <summary>
        /// Base64编码
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="objEncoding">字符编码实例，new ASCIIEncoding()</param>
        /// <returns>编码后的字符串</returns>
        public static string Base64Encode(this string target, Encoding objEncoding)
        {
            return Convert.ToBase64String(objEncoding.GetBytes(target));
        }

        /// <summary>
        /// Base64解码(UTF8)
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>解码后的字符串</returns>
        public static string Base64Decode(this string target)
        {
            return Encoding.UTF8.GetString(Convert.FromBase64String(target.Replace(" ", "+")));
        }

        /// <summary>
        /// Base64解码
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="objEncoding">字符编码实例，new ASCIIEncoding()</param>
        /// <returns>解码后的字符串</returns>
        public static string Base64Decode(this string target, Encoding objEncoding)
        {
            return objEncoding.GetString(Convert.FromBase64String(target.Replace(" ", "+")));
        }
        #endregion

        #region Html编码与解码
        /// <summary>
        /// Html编码
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>编码后的字符串</returns>
        public static string HtmlEncode(this string target)
        {
            return HttpContext.Current.Server.HtmlEncode(target);
        }

        /// <summary>
        /// Html解码
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>解码后的字符串</returns>
        public static string HtmlDecode(this string target)
        {
            return HttpContext.Current.Server.HtmlDecode(target);
        }
        #endregion

        #region Url编码与解码
        /// <summary>
        /// Url编码
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>编码后的字符串</returns>
        public static string UrlEncode(this string target)
        {
            return HttpContext.Current.Server.UrlEncode(target);
        }

        /// <summary>
        /// Url解码
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>解码后的字符串</returns>
        public static string UrlDecode(this string target)
        {
            return HttpContext.Current.Server.UrlDecode(target);
        }
        /// <summary>
        /// Url随机数
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>随机数后的字符串</returns>
        public static string UrlRandom(this string target)
        {
            return target += "?";
        }
        #endregion

        #region 标记过滤
        /// <summary>
        /// 过滤JavaScript标记
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>过滤后的字符串</returns>
        public static string FilterJavaScirpt(this string target)
        {
            Regex regex1 = new Regex(@"<script[\s\S]+</script *>", RegexOptions.IgnoreCase);
            Regex regex2 = new Regex(@"<script[^>]*?>.*?</script>", RegexOptions.IgnoreCase);
            Regex regex3 = new Regex(@"<script.*>[\s\S]*?</script>", RegexOptions.IgnoreCase);
            Regex regex4 = new Regex(@" href *= *[\s\S]*script *:", RegexOptions.IgnoreCase);
            Regex regex5 = new Regex(@" on[\s\S]*=", RegexOptions.IgnoreCase);
            Regex regex6 = new Regex(@"<iframe[^>]*?>.*?</iframe>", RegexOptions.IgnoreCase);
            Regex regex7 = new Regex(@"<frameset[^>]*?>.*?</frameset>", RegexOptions.IgnoreCase);

            target = regex1.Replace(target, "");
            target = regex2.Replace(target, "");
            target = regex3.Replace(target, "");
            target = regex4.Replace(target, "");
            target = regex5.Replace(target, "");
            target = regex6.Replace(target, "");
            target = regex7.Replace(target, "");

            return target;
        }

        /// <summary>
        /// 过滤Html标记
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>过滤后的字符串</returns>
        public static string FilterHtml(this string target)
        {
            //另一种方法
            //return new Regex("<[^>]+>").Replace(temp, "");
            Regex regex = new Regex(@"<\/*[^<>]*>", RegexOptions.IgnoreCase);

            return regex.Replace(target, "");
        }
        #endregion

        #region Txt与Html转换
        /// <summary>
        /// Txt转Html
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>Html</returns>
        public static string TxtToHtml(this string target)
        {
            target = target.Replace("&", "&amp;");
            target = target.Replace("'", "''");
            target = target.Replace("\"", "&quot;");
            target = target.Replace(" ", "&nbsp;");
            target = target.Replace("<", "&lt;");
            target = target.Replace(">", "&gt;");
            target = target.Replace("\n", "<br/>");

            return target;
        }

        /// <summary>
        /// Html转Txt
        /// </summary>
        /// <param name="str">字符串</param>
        /// <returns>Txt</returns>
        public static string HtmlToText(this string target)
        {
            target = target.Replace("&amp;", "&");
            target = target.Replace("''", "'");
            target = target.Replace("&quot;", "\"");
            target = target.Replace("&nbsp;", " ");
            target = target.Replace("&lt;", "<");
            target = target.Replace("&gt;", ">");
            target = target.Replace("<br/>", "\n");

            return target;
        }
        #endregion

        #region 字符串与List互转
        /// <summary>
        /// List转字符串
        /// </summary>
        /// <param name="target">List</param>
        /// <param name="separator">分隔符</param>
        /// <param name="beforeContainer">前包含符</param>
        /// <param name="afterContainer">后包含符</param>
        /// <returns>字符串</returns>
        public static string ListToString
        (
            this List<string> target,
            string separator,
            string beforeContainer,
            string afterContainer
        )
        {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < target.Count; i++)
            {
                if (beforeContainer != null)
                {
                    result.Append(beforeContainer);
                }

                result.Append(target[i]);

                if (afterContainer != null)
                {
                    result.Append(afterContainer);
                }

                if (i != target.Count - 1)
                {
                    result.Append(separator);
                }
            }

            return result.ToString();
        }

        /// <summary>
        /// 字符串转List
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="separator">分隔符</param>
        /// <returns>List</returns>
        public static List<string> StringToList(this string target, string separator)
        {
            List<string> result = new List<string>();

            {
                string[] temp = target.Split(separator.ToCharArray(0, separator.Length));

                result.AddRange(temp);
            }

            return result;
        }
        #endregion

        #region Other
        /// <summary>
        /// 是否是中文字符
        /// </summary>
        /// <param name="target">字符</param>
        /// <returns>true->是中文字符,false->不是中文字符</returns>
        public static bool IsChinese(this char target)
        {
            return (int)target > 0x4E00 && (int)target < 0x9FA5;
        }

        /// <summary>
        /// 字符串的首字母转为大写
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>转换后的字符串</returns>
        public static string FirstLetterToUpper(this string target)
        {
            return target.Substring(0, 1).ToUpper() + target.Remove(0, 1);
        }

        /// <summary>
        /// 字符串的首字母转为大写
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="prefix">前缀</param>
        /// <returns>转换后的字符串</returns>
        public static string FirstLetterToUpper(this string target, string prefix)
        {
            return prefix + target.Substring(0, 1).ToUpper() + target.Remove(0, 1);
        }

        /// <summary>
        /// 加入双引号
        /// </summary>
        /// <param name="target">字符串</param>
        /// <returns>加入双引号后的字符串</returns>
        public static string AddMarks(this string target)
        {
            string result = null;

            string[] temp = target.Replace("\r\n", "|").Split('|');

            /**/

            for (int i = 0; i < temp.Length; i++)
            {
                if (i < temp.Length - 1)
                {
                    result += "\"" + temp[i] + "\"" + " +" + "\r\n";
                }

                else
                {
                    result += "\"" + temp[i] + "\"";
                }
            }

            /**/

            return result;
        }

        /// <summary>
        /// 检查Html代码是否为空字符串
        /// </summary>
        /// <param name="target">Html代码</param>
        /// <returns>true->空字符串,false->非空字符串</returns>
        public static bool CheckHtmlIsEmpty(this string target)
        {
            bool result = false;

            {
                if (!String.IsNullOrEmpty(target))
                {
                    StringBuilder temp = new StringBuilder(target.ToLower());

                    temp.Replace("&nbsp;", String.Empty);
                    temp.Replace("<br>", String.Empty);
                    temp.Replace("<br/>", String.Empty);
                    temp.Replace("<br />", String.Empty);
                    temp.Replace("<p>", String.Empty);
                    temp.Replace("<p/>", String.Empty);
                    temp.Replace("<p />", String.Empty);

                    result = String.IsNullOrEmpty(temp.ToString().Trim());
                }
            }

            return result;
        }

        /// <summary>
        /// 转义字符
        /// </summary>
        /// <param name="target">字符串</param>
        /// <param name="escapeChars">需要转义的字符集合</param>
        /// <returns>转义后的字符</returns>
        public static string Escape(this string target, string[] escapeChars)
        {
            StringBuilder result = new StringBuilder(target);

            foreach (string escapeChar in escapeChars)
            {
                result.Replace(escapeChar, @"\" + escapeChar);
            }

            return result.ToString();
        }

        /// <summary>
        /// 删除最后结尾的指定字符后的字符
        /// </summary>
        public static string DeleteLastChar(this string target, string lastChar)
        {
            return target.Substring(0, target.LastIndexOf(lastChar));
        }
        #endregion
    }
}
