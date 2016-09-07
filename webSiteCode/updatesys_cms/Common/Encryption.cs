using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.IO;

namespace Common
{
    namespace Encryption
    {
        /// <summary>
        /// DES帮助类
        /// </summary>
        public static class DESHelper
        {
            //扩展方法，只需引用命名空间。
            //
            //DEMO: 
            //using Aigo.Common.Encryption;
            //namespace Aigo.Test
            //{
            //  public static void main()
            //  {
            //      string EncryptedString="password".DESEncrypt("KEY3LI8O");
            //      string DATA=EncryptedString.DESDecrypt("KEY3LI8O");
            //  }
            //}

            #region DESEncrypt DES加密
            /// <summary>
            /// 进行DES加密。
            /// </summary>
            /// <param name="pToEncrypt">要加密的字符串。</param>
            /// <param name="sKey">密钥，且必须为8位。</param>
            /// <returns>以Base64格式返回的加密字符串。</returns>
            public static string DESEncrypt(this string pToEncrypt, string sKey, string sIV)
            {
                using (DESCryptoServiceProvider des = new DESCryptoServiceProvider())
                {
                    if (sKey == null || sKey.Length != 8 || sIV == null || sIV.Length != 8) return null;
                    byte[] inputByteArray = Encoding.UTF8.GetBytes(pToEncrypt);
                    des.Key = System.Text.Encoding.UTF8.GetBytes(sKey);
                    des.IV = System.Text.Encoding.UTF8.GetBytes(sIV);
                    System.IO.MemoryStream ms = new System.IO.MemoryStream();
                    using (CryptoStream cs = new CryptoStream(ms, des.CreateEncryptor(), CryptoStreamMode.Write))
                    {
                        cs.Write(inputByteArray, 0, inputByteArray.Length);
                        cs.FlushFinalBlock();
                        cs.Close();
                    }
                    string str = Convert.ToBase64String(ms.ToArray());
                    ms.Close();
                    return str;
                }
            }
            #endregion

            #region DESDecrypt DES解密
            /// <summary>
            /// 进行DES解密。
            /// </summary>
            /// <param name="pToDecrypt">要解密的以Base64</param>
            /// <param name="sKey">密钥，8位。</param>
            /// <param name="sIV">向量，8位</param>
            /// <returns>已解密的字符串。</returns>
            public static string DESDecrypt(this string pToDecrypt, string sKey, string sIV)
            {
                if (sKey == null || sKey.Length != 8 || sIV == null || sIV.Length != 8) return null;
                byte[] inputByteArray = Convert.FromBase64String(pToDecrypt);
                using (DESCryptoServiceProvider des = new DESCryptoServiceProvider())
                {
                    des.Key = System.Text.Encoding.UTF8.GetBytes(sKey);
                    des.IV = System.Text.Encoding.UTF8.GetBytes(sIV);
                    System.IO.MemoryStream ms = new System.IO.MemoryStream();
                    string str = null;
                    try
                    {
                        using (CryptoStream cs = new CryptoStream(ms, des.CreateDecryptor(), CryptoStreamMode.Write))
                        {

                            cs.Write(inputByteArray, 0, inputByteArray.Length);
                            cs.FlushFinalBlock();
                            cs.Close();
                        }
                        str = Encoding.UTF8.GetString(ms.ToArray());
                    }
                    catch
                    {
                        str = null;
                    }
                    ms.Close();
                    return str;
                }
            }
            #endregion

            #region 3DES
            /// <summary>
            /// 3DES加密
            /// </summary>
            /// <param name="sData">明文</param>
            /// <param name="sKey">密文</param>
            /// <param name="sIV">矢量</param>
            /// <returns></returns>
            public static string TripleDESEncrypt(this string sData,byte[] sKey,byte[] sIV)
            {
                byte[] inputByteArray = Encoding.UTF8.GetBytes(sData);
                TripleDESCryptoServiceProvider provider = new TripleDESCryptoServiceProvider();
                provider.Key = sKey;
                provider.IV = sIV;
                try
                {
                    System.IO.MemoryStream ms = new System.IO.MemoryStream();
                    CryptoStream cStream = new CryptoStream(ms,
                        provider.CreateEncryptor(),
                        CryptoStreamMode.Write);
                    cStream.Write(inputByteArray, 0, inputByteArray.Length);
                    cStream.FlushFinalBlock();
                    cStream.Close();

                    string str = Convert.ToBase64String(ms.ToArray());
                    ms.Close();
                    return str;
                }
                catch
                {
                    //Console.WriteLine("A file access error occurred: {0}", e.Message);
                    return null;
                }
            }

            /// <summary>
            /// 3DES解密
            /// </summary>
            /// <param name="sData">密文</param>
            /// <param name="sKey">密钥</param>
            /// <param name="sIV">矢量</param>
            /// <returns></returns>
            public static string TripleDESDecrypt(this string sData, byte[] sKey, byte[] sIV)
            {
                byte[] inputByteArray = Convert.FromBase64String(sData);
                try
                {

                    System.IO.MemoryStream ms = new System.IO.MemoryStream();

                    CryptoStream cStream = new CryptoStream(ms,
                        new TripleDESCryptoServiceProvider().CreateDecryptor(sKey, sIV),
                        CryptoStreamMode.Write);

                    cStream.Write(inputByteArray, 0, inputByteArray.Length);
                    cStream.FlushFinalBlock();
                    string str = Encoding.UTF8.GetString(ms.ToArray());
                    cStream.Close();
                    return str;
                }
                catch
                {
                    //Console.WriteLine("A Cryptographic error occurred: {0}", e.Message);
                    return null;
                }
            }
            #endregion
        }

        public static class HashEncryption
        {
            #region MD5编码
            /// <summary>
            /// MD5
            /// </summary>
            /// <param name="inStr"></param>
            /// <returns></returns>
            public static string MD5Hash(this string inStr)
            {
                return System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(inStr, "MD5").ToLower();
            }
            #endregion

            #region SHA编码
            /// <summary>
            /// SHA1
            /// </summary>
            /// <param name="inStr"></param>
            /// <returns></returns>
            public static string SHAHash(this string inStr)
            {
                return System.Web.Security.FormsAuthentication.HashPasswordForStoringInConfigFile(inStr, "SHA1").ToLower();
            }
            #endregion
        }

        public class RSAHelper
        {
            private string _PublicKey, _PrivateKey;

            /// <summary>
            /// RSA帮助类
            /// </summary>
            /// <param name="publicKey">公钥（XML格式）</param>
            /// <param name="privateKey">私钥（XML格式）</param>
            public RSAHelper(string publicKey, string privateKey)
            {
                this._PublicKey = publicKey;
                this._PrivateKey = privateKey;
            }

            /// <summary>
            /// 加密数据（需要公钥）
            /// </summary>
            /// <param name="data"></param>
            /// <returns></returns>
            public string EncryptData(string data)
            {
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider(1024);
                //将公钥导入到RSA对象中，准备加密；
                rsa.FromXmlString(_PublicKey);
                //对数据data进行加密，并返回加密结果；
                //第二个参数用来选择Padding的格式
                return Convert.ToBase64String(rsa.Encrypt(System.Text.Encoding.UTF8.GetBytes(data), false));
            }


            /// <summary>
            /// 解密数据（需要私钥）
            /// </summary>
            /// <param name="data"></param>
            /// <returns></returns>
            public string DecryptData(string data)
            {
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider(1024);
                //将私钥导入RSA中，准备解密；
                rsa.FromXmlString(_PrivateKey);
                //对数据进行解密，并返回解密结果；
                return System.Text.Encoding.UTF8.GetString(rsa.Decrypt(Convert.FromBase64String(data), false));
            }
            /// <summary>
            /// 签名（需要私钥）
            /// </summary>
            /// <param name="data"></param>
            /// <returns></returns>
            public string Sign(string data)
            {
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider(1024);
                //导入私钥，准备签名
                rsa.FromXmlString(_PrivateKey);
                //将数据使用MD5进行消息摘要，然后对摘要进行签名并返回签名数据
                var result = rsa.SignData(System.Text.Encoding.UTF8.GetBytes(data), "MD5");
                return Convert.ToBase64String(result);
            }
            /// <summary>
            /// 验证签名（需要公钥）
            /// </summary>
            /// <param name="data"></param>
            /// <param name="Signature"></param>
            /// <returns></returns>
            public bool Verify(string data, string Signature)
            {
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider(1024);
                //导入公钥，准备验证签名
                rsa.FromXmlString(_PublicKey);
                //返回数据验证结果
                var sign = Convert.FromBase64String(Signature);
                return rsa.VerifyData(System.Text.Encoding.UTF8.GetBytes(data), "MD5", sign);
            }
        }
    }

}
