using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Web.Security;

namespace AppStore.Common
{
    /// <summary>
    /// 加密解密类
    /// </summary>
    public static class SecurityExtension
    {
        /// <summary>
        /// 获取字符串的MD5值
        /// </summary>
        /// <param name="target"></param>
        /// <param name="length"></param>
        /// <returns></returns>
        public static string MD5(this string target, int length = 32)
        {
            if (length == 16) //16位MD5加密（取32位加密的9~25字符） 
            {
                return FormsAuthentication.HashPasswordForStoringInConfigFile(target, "MD5").ToLower().Substring(8, 16);
            }
            else   //32位加密 
            {
                return FormsAuthentication.HashPasswordForStoringInConfigFile(target, "MD5").ToLower();
            }
        }

        /// <summary>
        /// 获取指定文件的MD5值
        /// </summary>
        /// <param name="target"></param>
        /// <returns></returns>
        public static string getFilesMD5Hash(this string target)
        {

            MD5CryptoServiceProvider md5 = new MD5CryptoServiceProvider();

            FileStream stream = new FileStream(target, FileMode.Open, FileAccess.Read, FileShare.Read, 8192);

            md5.ComputeHash(stream);

            stream.Close();

            byte[] hash = md5.Hash;

            StringBuilder sb = new StringBuilder();

            foreach (byte b in hash)
            {
                sb.Append(string.Format("{0:X2}", b));
            }

            return sb.ToString();
        }




        /// <summary>
        /// DES加密算法
        /// </summary>
        /// <param name="encryptString">要加密的字符串</param>
        /// <param name="sKey">加密码Key</param>
        /// <returns>正确返回加密后的结果，错误返回源字符串</returns>

        public static string ToDESEncrypt(string encryptString, string sKey, CipherMode mode = CipherMode.CBC)
        {
            try
            {
                byte[] keyBytes = Encoding.UTF8.GetBytes(sKey);
                byte[] keyIV = keyBytes;
                byte[] inputByteArray = Encoding.UTF8.GetBytes(encryptString);

                DESCryptoServiceProvider desProvider = new DESCryptoServiceProvider();
                // java 默认的是ECB模式，PKCS5padding；c#默认的CBC模式，PKCS7padding 所以这里我们默认使用ECB方式

                desProvider.Mode = mode;

                MemoryStream memStream = new MemoryStream();
                CryptoStream crypStream = new CryptoStream(memStream, desProvider.CreateEncryptor(keyBytes, keyIV), CryptoStreamMode.Write);

                crypStream.Write(inputByteArray, 0, inputByteArray.Length);
                crypStream.FlushFinalBlock();
                return Convert.ToBase64String(memStream.ToArray());

            }

            catch
            {
                return encryptString;
            }
        }


        /// <summary>
        /// DES解密算法
        /// </summary>
        /// <param name="decryptString">要解密的字符串</param>
        /// <param name="sKey">加密Key</param>
        /// <returns>正确返回加密后的结果，错误返回源字符串</returns>
        public static string ToDESDecrypt(string decryptString, string sKey, CipherMode mode = CipherMode.CBC)
        {

            byte[] keyBytes = Encoding.UTF8.GetBytes(sKey);
            byte[] keyIV = keyBytes;
            byte[] inputByteArray = Convert.FromBase64String(decryptString);

            DESCryptoServiceProvider desProvider = new DESCryptoServiceProvider();

            // java 默认的是ECB模式，PKCS5padding；c#默认的CBC模式，PKCS7padding 所以这里我们默认使用ECB方式
            desProvider.Mode = mode;

            MemoryStream memStream = new MemoryStream();

            CryptoStream crypStream = new CryptoStream(memStream, desProvider.CreateDecryptor(keyBytes, keyIV), CryptoStreamMode.Write);

            crypStream.Write(inputByteArray, 0, inputByteArray.Length);

            crypStream.FlushFinalBlock();

            return Encoding.Default.GetString(memStream.ToArray());

        }


    }

    public static class DESHelper
    {
        // key, iv 要求是8位的字符
        private static string strKey = "z#!.~ykZ";
        private static string strIv = "pz!#yk*t";

        /// <summary>
        /// 进行DES加密。
        /// </summary>
        /// <param name="pToEncrypt">要加密的字符串。</param>
        /// <param name="sKey">密钥，且必须为8位。</param>
        /// <returns>返回加密后的十六进制字符串。</returns>
        public static string Encrypt(this string pToEncrypt)
        {
            using (DESCryptoServiceProvider des = new DESCryptoServiceProvider())
            {
                byte[] inputByteArray = Encoding.UTF8.GetBytes(pToEncrypt);

                des.Key = ASCIIEncoding.ASCII.GetBytes(strKey);
                des.IV = ASCIIEncoding.ASCII.GetBytes(strIv);
                System.IO.MemoryStream ms = new System.IO.MemoryStream();
                using (CryptoStream cs = new CryptoStream(ms, des.CreateEncryptor(), CryptoStreamMode.Write))
                {
                    cs.Write(inputByteArray, 0, inputByteArray.Length);
                    cs.FlushFinalBlock();
                    cs.Close();
                }
                //string str = Convert.ToBase64String(ms.ToArray());
                string str = DESHelper.ByteToString(ms.ToArray());
                ms.Close();
                return str;
            }
        }

        /// <summary>
        /// 进行DES解密。
        /// </summary>
        /// <param name="pToDecrypt">要解密的以十六进制字符串</param>
        /// <param name="sKey">密钥，且必须为8位。</param>
        /// <returns>已解密的字符串。</returns>
        public static string Decrypt(this string pToDecrypt)
        {
            //byte[] inputByteArray = Convert.FromBase64String(pToDecrypt);
            byte[] inputByteArray = DESHelper.StringToByte(pToDecrypt);
            using (DESCryptoServiceProvider des = new DESCryptoServiceProvider())
            {
                des.Key = ASCIIEncoding.ASCII.GetBytes(strKey);
                des.IV = ASCIIEncoding.ASCII.GetBytes(strIv);
                System.IO.MemoryStream ms = new System.IO.MemoryStream();
                using (CryptoStream cs = new CryptoStream(ms, des.CreateDecryptor(), CryptoStreamMode.Write))
                {
                    cs.Write(inputByteArray, 0, inputByteArray.Length);
                    cs.FlushFinalBlock();
                    cs.Close();
                }
                string str = Encoding.UTF8.GetString(ms.ToArray());
                ms.Close();
                return str;
            }
        }

        public static string ByteToString(byte[] InBytes)
        {
            string stringOut = "";
            foreach (byte InByte in InBytes)
            {
                stringOut += InByte.ToString("x2");
            }
            return stringOut;
        }


        public static byte[] StringToByte(string hexString)
        {
            hexString = hexString.Replace(" ", "");
            if ((hexString.Length % 2) != 0)
                hexString += " ";
            byte[] returnBytes = new byte[hexString.Length / 2];
            for (int i = 0; i < returnBytes.Length; i++)
                returnBytes[i] = Convert.ToByte(hexString.Substring(i * 2, 2), 16);
            return returnBytes;
        }
    }
}
