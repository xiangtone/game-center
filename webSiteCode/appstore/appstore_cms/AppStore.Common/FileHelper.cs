using System;
using System.Data;
using System.IO;
using System.Text;
using System.Threading;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Common
{
    /// <summary>
    /// FileHelper
    /// </summary>
    [Serializable]
    public class FileHelper
    {
        #region Public Static Method

        #region 检查文件及文件目录是否存在
        /// <summary>
        /// 检查文件是否存在
        /// </summary>
        /// <param name="filePath">文件相对路径</param>
        /// <returns></returns>
        public static bool CheckExists(string filePath)
        {
            return System.IO.File.Exists(FileHelper.ConvertPath(filePath));
        }

        /// <summary>
        /// 检查目录是否存在
        /// </summary>
        /// <param name="directoryPath">目录相对路径</param>
        /// <returns></returns>
        public static bool CheckDirectoryExists(string directoryPath)
        {
            return Directory.Exists(FileHelper.ConvertPath(directoryPath));
        }

        /// <summary>
        /// 创建目录
        /// </summary>
        /// <param name="directoryPath">目录相对路径</param>
        /// <param name="isDeleteExists">是否删除已存在同名目录</param>
        public static void CreateDirectory(string directoryPath, bool isDeleteExists)
        {
            string currentDirectoryPath = FileHelper.ConvertPath(directoryPath);

            if (FileHelper.CheckDirectoryExists(directoryPath) && isDeleteExists)
            {
                Directory.Delete(currentDirectoryPath);
            }

            Directory.CreateDirectory(currentDirectoryPath);
        }
        #endregion

        #region 删除文件及文件目录
        /// <summary>
        /// 删除文件
        /// </summary>
        /// <param name="filePath">文件相对路径</param>
        public static void DeleteFile(string filePath)
        {
            FileHelper.DeleteFile(filePath, false);
        }

        /// <summary>
        /// 删除文件
        /// </summary>
        /// <param name="filePath">文件路径</param>
        /// <param name="isAbsolute">是否是相对路径</param>
        public static void DeleteFile(string filePath, bool isAbsolute)
        {
            System.IO.File.Delete(isAbsolute ? filePath : FileHelper.ConvertPath(filePath));
        }

        /// <summary>
        /// 删除目录
        /// </summary>
        /// <param name="directoryPath">目录相对路径</param>
        public static void DeleteDirectory(string directoryPath)
        {
            FileHelper.DeleteDirectory(directoryPath, false);
        }

        /// <summary>
        /// 删除目录
        /// </summary>
        /// <param name="directoryPath">目录路径</param>
        /// <param name="isAbsolute">是否是绝对路径</param>
        public static void DeleteDirectory(string directoryPath, bool isAbsolute)
        {
            Directory.Delete(isAbsolute ? directoryPath : FileHelper.ConvertPath(directoryPath), true);
        }
        #endregion

        #region 文件名与大小
        /// <summary>
        /// 根据GUID获取文件名
        /// </summary>
        /// <param name="extensionName">文件扩展名</param>
        /// <returns>文件名</returns>
        public static string GetFileNamebyGuid(string extensionName)
        {
            return Guid.NewGuid().ToString() + extensionName;
        }

        /// <summary>
        /// 根据时间获取文件名
        /// </summary>
        /// <param name="extensionName">文件扩展名</param>
        /// <returns>文件名</returns>
        public static string GetFileNameByDateTime(string extensionName)
        {
            return FileHelper.GetFileNameByDateTime(true) + extensionName;
        }

        /// <summary>
        /// 根据时间获取文件名(不带文件扩展名)
        /// </summary>
        /// <param name="isLong">是否是长时间</param>
        /// <returns>文件名</returns>
        public static string GetFileNameByDateTime(bool isLong)
        {
            return isLong ? DateTime.Now.ToString("yyyyMMddHHmmssffff") : DateTime.Now.ToString("yyyyMMdd");
        }

        /// <summary>
        /// 获取文件扩展名
        /// </summary>
        /// <param name="fileName">文件名</param>
        /// <returns>文件扩展名</returns>
        public static string GetFileExtensionName(string fileName)
        {
            string result = String.Empty;

            if (!string.IsNullOrEmpty(fileName))
            {
                result = new FileInfo(fileName).Extension;
            }

            return result;
        }

        /// <summary>
        /// 获取文件大小
        /// </summary>
        /// <param name="bytes">字节</param>
        /// <returns>文件大小</returns>
        public static string GetFileSize(long bytes)
        {
            string result = "0 Bytes";

            {
                if (bytes >= 1073741824)
                {
                    result = String.Format("{0:##.##} GB", Decimal.Divide(bytes, 1073741824));
                }

                else if (bytes >= 1048576)
                {
                    result = String.Format("{0:##.##} MB", Decimal.Divide(bytes, 1048576));
                }

                else if (bytes >= 1024)
                {
                    result = String.Format("{0:##.##} KB", Decimal.Divide(bytes, 1024));
                }

                else if (bytes > 0 & bytes < 1024)
                {
                    result = String.Format("{0:##.##} Bytes", bytes);
                }

            }

            return result;
        }
        #endregion

        #region 操作文件
        /// <summary>
        /// 获取文件列表
        /// </summary>
        /// <param name="folderPath">文件夹路径</param>
        /// <param name="isAbsolute">是否是绝对路径</param>
        /// <returns>文件列表</returns>
        public static DataTable GetFileList(string folderPath, bool isAbsolute)
        {
            DataTable result = new DataTable();

            result.Columns.Add(new DataColumn("FileName", typeof(string)));
            result.Columns.Add(new DataColumn("FileSize", typeof(string)));
            result.Columns.Add(new DataColumn("CreateTime", typeof(string)));
            result.Columns.Add(new DataColumn("LastAccessTime", typeof(string)));

            {
                if (!isAbsolute)
                {
                    folderPath = FileHelper.ConvertPath(folderPath);
                }

                FileInfo[] files = new DirectoryInfo(folderPath).GetFiles();

                if (files.Length > 0)
                {
                    DataRow objDR = null;

                    foreach (FileInfo objFI in files)
                    {
                        objDR = result.NewRow();

                        objDR["FileName"] = objFI.Name;
                        objDR["FileSize"] = FileHelper.GetFileSize(objFI.Length);
                        objDR["CreateTime"] = objFI.CreationTime.ToString();
                        objDR["LastAccessTime"] = objFI.LastAccessTime.ToString();

                        result.Rows.Add(objDR);
                    }
                }
            }

            return result;
        }

        /// <summary>
        /// 获取目录列表
        /// </summary>
        /// <param name="folderPath">文件夹路径</param>
        /// <param name="isAbsolute">是否是绝对路径</param>
        /// <returns>目录列表</returns>
        public static string[] GetDirectoryList(string folderPath, bool isAbsolute)
        {
            return null;
        }

        /// <summary>
        /// 读取文件
        /// </summary>
        /// <param name="filePath">文件路径</param>
        /// <param name="isAbsolute">是否是绝对路径</param>
        /// <returns>文件内容</returns>
        public static string ReadFile(string filePath, bool isAbsolute)
        {
            string result = null;

            lock (new object())
            {
                string temp = isAbsolute ? filePath : HttpContext.Current.Server.MapPath(filePath);

                using (FileStream objFS = new FileStream(temp, FileMode.Open, FileAccess.Read, FileShare.None))
                {
                    using (StreamReader objSR = new StreamReader(objFS))
                    {
                        result = objSR.ReadToEnd();

                        objSR.Close();
                        objFS.Close();
                    }
                }
            }

            return result;
        }

        /// <summary>
        /// 写入文件
        /// </summary>
        /// <param name="filePath">文件路径</param>
        /// <param name="isAbsolute">是否是绝对路径</param>
        /// <param name="fileContent">文件内容</param>
        public static void WriteFile(string filePath, bool isAbsolute, string fileContent)
        {
            lock (new object())
            {
                string temp = isAbsolute ? filePath : HttpContext.Current.Server.MapPath(filePath);

                using (FileStream objFS = new FileStream(temp, FileMode.Create, FileAccess.Write, FileShare.None))
                {
                    using (StreamWriter objSW = new StreamWriter(objFS))
                    {
                        objSW.Write(fileContent);
                        objSW.Flush();

                        objSW.Close();
                        objFS.Close();
                    }
                }
            }
        }

        /// <summary>
        /// 下载文件
        /// </summary>
        /// <param name="realFilePath">文件相对路径</param>
        /// <param name="rename">重命名</param>
        /// <param name="isFinishDelete">下载完成后是否删除文件</param>
        public static void DownloadFile(string realFilePath, string rename, bool isFinishDelete)
        {
            string path = FileHelper.ConvertPath(realFilePath);

            if (FileHelper.CheckExists(path))
            {
                HttpContext.Current.Response.Clear();
                HttpContext.Current.Response.Charset = "UTF-8";
                HttpContext.Current.Response.ContentType = "application/octet-stream";
                HttpContext.Current.Response.ContentEncoding = Encoding.UTF8;
                HttpContext.Current.Response.AddHeader("Connection", "Keep-Alive");
                HttpContext.Current.Response.AddHeader("Content-Disposition", "attachment;filename=" + HttpUtility.UrlEncode(rename, System.Text.Encoding.UTF8));
                HttpContext.Current.Response.AddHeader("Content-Transfer-Encoding", "binary");

                using (FileStream objFS = new FileStream(path, FileMode.Open, FileAccess.Read, FileShare.ReadWrite))
                {
                    int size = 1024;
                    int length = 0;

                    for (int i = 0; i < objFS.Length / size + 1; i++)
                    {
                        byte[] buffer = new byte[size];

                        length = objFS.Read(buffer, 0, size);

                        if (length > 0)
                        {
                            HttpContext.Current.Response.OutputStream.Write(buffer, 0, length);
                            HttpContext.Current.Response.Flush();
                        }
                    }

                    objFS.Close();
                }

                if (isFinishDelete)
                {
                    FileHelper.DeleteFile(realFilePath);
                }
            }
        }

        /// <summary>
        /// 使用页面下载文件
        /// </summary>
        /// <param name="realFilePath">文件相对路径</param>
        /// <param name="rename">重命名</param>
        public static void DownloadFileWithPage(string realFilePath, string rename)
        {
            HttpContext.Current.Response.Redirect("~/App_Page/Public/FileDownload.aspx?RealFilePath={0}&Rename={1}".FormatWith(realFilePath, rename));
        }

        /// <summary>
        /// 获取下载文件网址
        /// </summary>
        /// <param name="realFilePath">文件相对路径</param>
        /// <param name="rename">重命名</param>
        /// <returns></returns>
        public static string GetDownloadFileUrl(string realFilePath, string rename)
        {
            return "~/App_Page/Public/FileDownload.aspx?RealFilePath={0}&Rename={1}".FormatWith(realFilePath.UrlEncode(), rename.UrlEncode());
        }
        #endregion

        #region 其他
        /// <summary>
        /// 转换路径
        /// </summary>
        /// <param name="relativePath">相对路径</param>
        /// <returns>绝对路径</returns>
        public static string ConvertPath(string relativePath)
        {
            if (!relativePath.Contains(@":\"))
            {
                return HttpContext.Current.Server.MapPath(relativePath);
            }

            else
            {
                return relativePath;
            }
        }
        #endregion

        #endregion
    }
}
