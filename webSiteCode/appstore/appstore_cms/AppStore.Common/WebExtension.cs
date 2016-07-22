using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;
using System.Web.UI.WebControls;

namespace AppStore.Common
{
    /// <summary>
    /// Web相关常用方法
    /// </summary>
    public static class WebExtension
    {

        /// <summary>
        /// 获取请求的流，并返回对应的字符串
        /// </summary>
        /// <param name="bs"></param>
        /// <returns></returns>
        public static string InputStreamToString(this byte[] bs)
        {
            HttpContext.Current.Request.InputStream.Read(bs, 0, bs.Length);
            return Encoding.Default.GetString(bs);
        }

        /// <summary>
        /// HTML Encode
        /// </summary>
        /// <param name="target"></param>
        /// <returns></returns>
        public static string HtmlEncode(this object target)
        {
            if (target != null)
            {
                return HttpUtility.HtmlEncode(target);
            }
            return string.Empty;
        }

        /// <summary>
        /// HTML Decode
        /// </summary>
        /// <param name="target"></param>
        /// <returns></returns>
        public static string HtmlDecode(this string target)
        {
            if (target != null)
            {
                return HttpUtility.HtmlDecode(target);
            }
            return string.Empty;
        }

        /// <summary>
        /// 追加到IIS日志
        /// </summary>
        /// <param name="target"></param>
        public static void AppendToLog(this string target)
        {
            HttpContext.Current.Response.AppendToLog(target);
        }

        /// <summary>
        /// 将传统的请求参数转换成Dictionary
        /// </summary>
        /// <param name="target"></param>
        /// <param name="flag"></param>
        /// <returns></returns>
        public static Dictionary<string, string> ToDictionary(this string[] target, char flag = '=')
        {
            Dictionary<string, string> dic = new Dictionary<string, string>();

            for (int i = 0; i < target.Length; i++)
            {
                string[] keyvalue = target[i].Split(flag);
                dic.Add(keyvalue[0], keyvalue[1]);
            }

            return dic;
        }

        /// <summary>
        /// 向指定的URL发起Post请求
        /// </summary>
        /// <param name="requestUrl"></param>
        /// <param name="requestParams"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public static string Post(string requestUrl, string requestParams, string type = "text/html")
        {
            string result = string.Empty;
            byte[] bs = Encoding.Default.GetBytes(requestParams);
            Stream reqStream = null;

            WebRequest objRequest = WebRequest.Create(requestUrl);
            objRequest.Method = "POST";
            objRequest.ContentLength = requestParams.Length;
            objRequest.ContentType = type;
            objRequest.Timeout = 2000;
            try
            {
                using (reqStream = objRequest.GetRequestStream())
                {
                    reqStream.Write(bs, 0, bs.Length);
                }

            }
            catch (Exception ex)
            {
                LogHelper.Default.Error(ex.ToString());
                return string.Empty;
            }
            finally
            {
                reqStream.Close();
            }

            WebResponse objResponse = objRequest.GetResponse();
            using (StreamReader sr = new StreamReader(objResponse.GetResponseStream()))
            {
                result = sr.ReadToEnd();
                sr.Close();
            }
            return result;
        }


        /// <summary>
        /// 上传文件
        /// </summary>
        /// <param name="objFileUpload"></param>
        /// <param name="url"></param>
        /// <param name="allowedExtensions">允许上传的文件扩展名</param>
        /// <param name="bytes"></param>
        /// <returns></returns>
        public static string UploadFiles(this FileUpload objFileUpload, string url, string[] allowedExtensions, byte[] bytes)
        {
            string savePath = HttpContext.Current.Server.MapPath("~/uploads/");
            string fileName = DateTime.Now.ToString("yyyyMMddhhmmssfff");
            String fileExtension = Path.GetExtension(objFileUpload.FileName).ToLower();
            bool fileOK = false;

            if (objFileUpload.HasFile)
            {
                if (!Directory.Exists(savePath))
                {
                    Directory.CreateDirectory(savePath);
                }

                for (int i = 0; i < allowedExtensions.Length; i++)
                {
                    if (fileExtension == allowedExtensions[i])
                    {
                        fileOK = true;
                    }
                }

                if (fileOK)
                {
                    objFileUpload.SaveAs(string.Format("{0}{1}", savePath, fileName));
                    return string.Format("{0}/uploads/{1}{2}", url, fileName, fileExtension);
                }
                else
                {
                    return string.Empty;
                }

            }
            else
            {
                return string.Empty;
            }
        }

    }
}
