using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;
using System.IO;
using System.Net;


namespace AppStore.Common
{
    /// <summary>
    /// ImageHelper
    /// </summary>
    [Serializable]
    public class ImageHelper
    {
        #region Public Enum
        /// <summary>
        /// 水印位置
        /// </summary>
        public enum WaterMarkPosition
        {
            /// <summary>
            /// 左上角
            /// </summary>
            LeftTop = 1,

            /// <summary>
            /// 左下角
            /// </summary>
            LeftBottom = 2,

            /// <summary>
            /// 右上角
            /// </summary>
            RightTop = 3,

            /// <summary>
            /// 右下角
            /// </summary>
            RightBottom = 4
        }
        #endregion

        #region Public Static Method
        /// <summary>
        /// 按比例无失真缩放图片
        /// </summary>
        /// <param name="sourceImageFile">源图片路径，C:\Test.jpg</param>
        /// <param name="resultImageFile">结果图片保存路径，D:\Test.jpg</param>
        /// <param name="destWidth">期望宽度</param>
        /// <param name="destHeight">期望高度</param>
        /// <param name="addBackground">是否添加背景</param>
        /// <returns>缩放是否成功</returns>
        public static bool Zoom
        (
            string sourceImageFile,
            string resultImageFile,
            int destWidth,
            int destHeight,
            bool addBackground
        )
        {
            bool result = false;

            {
                if (FileHelper.CheckExists(sourceImageFile))
                {
                    using (System.Drawing.Image sourceImage = System.Drawing.Image.FromFile(sourceImageFile))
                    {
                        int width = 0;
                        int height = 0;

                        #region 得到缩放后的高度和宽度
                        if (sourceImage.Height > destHeight || sourceImage.Width > destWidth)
                        {
                            if ((sourceImage.Width * destHeight) > (sourceImage.Height * destWidth))
                            {
                                width = destWidth;
                                height = (destWidth * sourceImage.Height) / sourceImage.Width;
                            }

                            else
                            {
                                height = destHeight;
                                width = (sourceImage.Width * destHeight) / sourceImage.Height;
                            }
                        }

                        else
                        {
                            height = sourceImage.Height;
                            width = sourceImage.Width;
                        }
                        #endregion

                        using (Bitmap resultBitmap = (addBackground ? new Bitmap(destWidth, destHeight) : new Bitmap(width, height)))
                        {
                            using (Graphics g = Graphics.FromImage(resultBitmap))
                            {
                                #region 设置画笔并画图
                                g.Clear(Color.WhiteSmoke);

                                g.CompositingQuality = CompositingQuality.HighQuality;
                                g.SmoothingMode = SmoothingMode.HighQuality;
                                g.InterpolationMode = InterpolationMode.HighQualityBicubic;

                                if (addBackground)
                                {
                                    g.DrawImage
                                    (
                                        sourceImage,
                                        new Rectangle((destWidth - width) / 2, (destHeight - height) / 2, width, height),
                                        new Rectangle(0, 0, sourceImage.Width, sourceImage.Height),
                                        GraphicsUnit.Pixel
                                    );
                                }

                                else
                                {
                                    g.DrawImage
                                    (
                                        sourceImage,
                                        new Rectangle(0, 0, width, height),
                                        new Rectangle(0, 0, sourceImage.Width, sourceImage.Height),
                                        GraphicsUnit.Pixel
                                    );
                                }
                                #endregion

                                #region 设置保存图片时的压缩质量
                                EncoderParameters eps = new EncoderParameters();
                                long[] quality = new long[1];
                                quality[0] = 100;

                                EncoderParameter ep = new EncoderParameter(Encoder.Quality, quality);
                                eps.Param[0] = ep;
                                #endregion

                                #region 获取图片编码信息
                                ImageCodecInfo[] icis = ImageCodecInfo.GetImageEncoders();
                                ImageCodecInfo ici = null;

                                for (int i = 0; i < icis.Length; i++)
                                {
                                    if (icis[i].FormatDescription.ToUpper().Equals("JPEG"))
                                    {
                                        ici = icis[i];

                                        break;
                                    }
                                }
                                #endregion

                                #region 保存图片
                                if (ici != null)
                                {
                                    resultBitmap.Save(resultImageFile, ici, eps);
                                }

                                else
                                {
                                    resultBitmap.Save(resultImageFile, sourceImage.RawFormat);
                                }
                                #endregion
                            }
                        }

                        result = true;
                    }
                }
            }

            return result;
        }

        /// <summary>
        /// 给图片添加图片水印
        /// </summary>
        /// <param name="sourceImageFile">源图片路径</param>
        /// <param name="waterMarkImageFile">水印图片路径</param>
        /// <param name="resultImageFile">结果图片保存路径</param>
        /// <param name="position">水印在图片中的位置</param>
        /// <param name="deleteSouceImageFile">是否删除源图片</param>
        /// <returns>添加图片水印是否成功</returns>
        public static bool AddImageWaterMark
        (
            string sourceImageFile,
            string waterMarkImageFile,
            string resultImageFile,
            WaterMarkPosition position,
            bool deleteSouceImageFile
        )
        {
            bool result = false;

            {
                if (FileHelper.CheckExists(sourceImageFile) && FileHelper.CheckExists(waterMarkImageFile))
                {
                    using (System.Drawing.Image sourceImage = System.Drawing.Image.FromFile(sourceImageFile))
                    {
                        using (System.Drawing.Image waterMarkImage = System.Drawing.Image.FromFile(waterMarkImageFile))
                        {
                            using (Graphics g = Graphics.FromImage(sourceImage))
                            {
                                switch (position)
                                {
                                    #region LeftTop
                                    case WaterMarkPosition.LeftTop:
                                        g.DrawImage
                                        (
                                            waterMarkImage,
                                            new Rectangle(0, 0, waterMarkImage.Width, waterMarkImage.Height),
                                            new Rectangle(0, 0, waterMarkImage.Width, waterMarkImage.Height),
                                            GraphicsUnit.Pixel
                                        );
                                        break;
                                    #endregion

                                    #region LeftBottom
                                    case WaterMarkPosition.LeftBottom:
                                        g.DrawImage
                                        (
                                            waterMarkImage,
                                            new Rectangle(0, sourceImage.Height - waterMarkImage.Height, waterMarkImage.Width, waterMarkImage.Height),
                                            new Rectangle(0, 0, waterMarkImage.Width, waterMarkImage.Height),
                                            GraphicsUnit.Pixel
                                        );
                                        break;
                                    #endregion

                                    #region RightTop
                                    case WaterMarkPosition.RightTop:
                                        g.DrawImage
                                        (
                                            waterMarkImage,
                                            new Rectangle(sourceImage.Width - waterMarkImage.Width, 0, waterMarkImage.Width, waterMarkImage.Height),
                                            new Rectangle(0, 0, waterMarkImage.Width, waterMarkImage.Height),
                                            GraphicsUnit.Pixel
                                        );
                                        break;
                                    #endregion

                                    #region RightBottom
                                    case WaterMarkPosition.RightBottom:
                                        g.DrawImage
                                        (
                                            waterMarkImage,
                                            new Rectangle(sourceImage.Width - waterMarkImage.Width, sourceImage.Height - waterMarkImage.Height, waterMarkImage.Width, waterMarkImage.Height),
                                            new Rectangle(0, 0, waterMarkImage.Width, waterMarkImage.Height),
                                            GraphicsUnit.Pixel
                                        );
                                        break;
                                    #endregion
                                }

                                sourceImage.Save(resultImageFile, sourceImage.RawFormat);
                            }
                        }
                    }

                    if (deleteSouceImageFile)
                    {
                        FileHelper.DeleteFile(sourceImageFile, true);
                    }

                    result = true;
                }
            }

            return result;
        }
        #endregion

        /// <summary>
        /// 在原图基础上指定坐标裁剪指定长和宽的图片
        /// </summary>
        /// <param name="b"></param>
        /// <param name="StartX"></param>
        /// <param name="StartY"></param>
        /// <param name="iWidth"></param>
        /// <param name="iHeight"></param>
        /// <returns></returns>
        public static Bitmap KiCut(Bitmap b, float StartX, float StartY, float iWidth, float iHeight)
        {
            if (b == null)
            {
                return null;
            }
            int w = b.Width;
            int h = b.Height;
            if (StartX >= w || StartY >= h)
            {
                return null;
            }
            if (StartX + iWidth > w)
            {
                iWidth = w - StartX;
            }
            if (StartY + iHeight > h)
            {
                iHeight = h - StartY;
            }
            try
            {
                Bitmap bmpOut = new Bitmap((int)iWidth, (int)iHeight, PixelFormat.Format24bppRgb);
                Graphics g = Graphics.FromImage(bmpOut);
                g.DrawImage(b, new Rectangle(0, 0, (int)iWidth, (int)iHeight), new Rectangle((int)StartX, (int)StartY, (int)iWidth, (int)iHeight), GraphicsUnit.Pixel);
                g.Dispose();
                return bmpOut;
            }
            catch
            {
                return null;
            }
        }




        /// <summary>
        /// 将远程图片转成Bitmap对象
        /// </summary>
        /// <param name="url"></param>
        /// <returns></returns>
        public static Bitmap GetBitmapFromUrl(string url)
        {
            if (string.IsNullOrEmpty(url))
            {
                return null;
            }
            else
            {
                WebClient client = new WebClient();
                byte[] bs = client.DownloadData(url);
                MemoryStream ms = new MemoryStream();
                ms.Write(bs, 0, bs.Length);
                return new Bitmap(ms);
            }
        }

        /// <summary>
        /// 压缩图片
        /// </summary>
        /// <param name="srcFile"></param>
        /// <param name="width"></param>
        /// <param name="height"></param>
        /// <returns></returns>
        public static Bitmap ImageCompress(Bitmap srcFile, int width, int height)
        {
            Bitmap b = new Bitmap(width, height);
            Bitmap src = new Bitmap(srcFile);
            Graphics g = Graphics.FromImage(b);

            g.CompositingQuality = CompositingQuality.AssumeLinear;
            g.CompositingMode = CompositingMode.SourceCopy;
            g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.HighQuality;
            g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;
            g.DrawImage(src, new Rectangle(0, 0, width, height));

            return b;
        }
    }
}
