using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Common
{
    /// <summary>
    /// 类型转换帮助类
    /// </summary>
    public static class ConvertHelper
    {
        public static String ToString(Object obj)
        {
            if (obj == DBNull.Value || obj == null)
                return string.Empty;
            else
                return obj.ToString();
        }

        public static int ToInt(Object obj)
        {
            int Default = 0;
            if (obj == DBNull.Value || obj == null)
                return 0;
            else
                int.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static bool ToBool(Object obj)
        {
            bool Default = false;
            if (obj == DBNull.Value || obj == null)
                return false;
            else
            {
                if (!bool.TryParse(obj.ToString(), out Default))
                    if (obj.ToString() == "1")
                        Default = true;
            }
            return Default;
        }

        public static DateTime ToDateTime(Object obj)
        {
            DateTime Default = new DateTime(1900, 01, 01);
            if (obj == DBNull.Value || obj == null)
                return Default;
            else
                DateTime.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static DateTime ToDateTime(Object obj, string timeFormat)
        {
            DateTime v = new DateTime(1900, 01, 01);
            if (!(obj == DBNull.Value || obj == null))
            {
                // string format = "dd/MMM/yyyy:HH:mm:ss";
                // yyyy-MM-ddTHH:mm:ss.ffffffzzz
                System.Globalization.CultureInfo cultureInfo = new System.Globalization.CultureInfo("en-US");
                DateTime.TryParseExact(obj.ToString(), timeFormat, cultureInfo, System.Globalization.DateTimeStyles.None, out v);
            }
            return v;
        }

        public static decimal ToDecimal(Object obj)
        {
            decimal Default = 0;
            if (obj == DBNull.Value || obj == null)
                return 0;
            else
                decimal.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static float ToFloat(Object obj)
        {
            float Default = 0F;
            if (obj == DBNull.Value || obj == null)
                return 0F;
            else
                float.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static double ToDouble(Object obj)
        {
            double Default = 0L;
            if (obj == DBNull.Value || obj == null)
                return 0;
            else
                double.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static short ToInt16(Object obj)
        {
            short Default = 0;
            if (obj == DBNull.Value || obj == null)
                return 0;
            else
                short.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static long ToInt64(Object obj)
        {
            long Default = 0L;
            if (obj == DBNull.Value || obj == null)
                return 0L;
            else
                long.TryParse(obj.ToString(), out Default);
            return Default;
        }

        public static byte[] ToByte(Object obj)
        {
            if (obj == DBNull.Value || obj == null || !(obj is byte[]))
                return null;
            else
                return (byte[])obj;
        }
    }
}
