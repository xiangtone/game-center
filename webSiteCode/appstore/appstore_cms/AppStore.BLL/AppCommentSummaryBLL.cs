using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class AppCommentSummaryBLL
    {
        public bool Delete(int AppId)
        {
            return new AppCommentSummaryDAL().Delete(AppId);
        }
        public bool Insert(AppCommentSummaryEntity entity)
        {
            return new AppCommentSummaryDAL().Insert(entity);
        }
        /// <summary>
        /// 修改应用评星
        /// </summary>
        /// <param name="appID"></param>
        /// <param name="DownTimes"></param>
        /// <param name="RecommLevel"></param>
        public void UpdateSummary(int appID, int DownTimes, int RecommLevel)
        {
            Random ran = new Random();
            double count = 0;
            if (DownTimes > 1000)
            {
                double ttimes = 0;
                double tcount = 0;
                if (DownTimes > 1100)
                {
                    ttimes = ran.Next(800, 1100);
                }
                else
                {
                    ttimes = ran.Next(800, 1000);
                }
                tcount = DownTimes / ttimes;
                double scale_random = 0;
                if (tcount < 5)
                {
                    scale_random = ran.Next(10, 20) / 10.0;
                }
                else
                {
                    scale_random = ran.Next(7, 15) / 10.0;
                }
                count = tcount * scale_random;
                if (count == 1)
                {
                    count = ran.Next(1, 9);
                }
            }
            else
            {
                count = ran.Next(5, 20);
            }
            if (count > 10)
            {
                string [] scales =new string[5];
                if (count > 5000 && count <= 10000 && RecommLevel < 6)
                {
                    scales = get_scale(6);
                }
                if (count > 10000 && RecommLevel < 6)
                {
                    scales = get_scale(7);
                }
                else
                {
                    scales = get_scale(RecommLevel);
                }
                int ScoreTimes5 = getInt(count * double.Parse(scales[0]), "floor");
                int ScoreTimes4 = getInt(count * double.Parse(scales[1]), "floor");
                int ScoreTimes3 = getInt(count * double.Parse(scales[2]), "floor");
                int ScoreTimes2 = getInt(count * double.Parse(scales[3]), "floor");
                int ScoreTimes1 = getInt(count * double.Parse(scales[4]), "floor");
                int ScoreTimes = ScoreTimes5 + ScoreTimes4 + ScoreTimes3 + ScoreTimes2 + ScoreTimes1;
                int ScoreSum = ScoreTimes5 * 5 + ScoreTimes4 * 4 + ScoreTimes3 * 3 + ScoreTimes2 * 2 + ScoreTimes1;
                int ScoreAvg = getInt(((double)ScoreSum * 2 / (double)ScoreTimes), "ceil");
                AppCommentSummaryEntity entity = new AppCommentSummaryEntity() { AppID = appID, CommentTimes = 0, ScoreSum = ScoreSum, ScoreTimes = ScoreTimes, ScoreAvg = ScoreAvg, ScoreTimes1 = ScoreTimes1, ScoreTimes2 = ScoreTimes2, ScoreTimes3 = ScoreTimes3, ScoreTimes4 = ScoreTimes4, ScoreTimes5 = ScoreTimes5 };
                if (Delete(appID))
                {
                    Insert(entity);
                }
            }
            else
            {
                int ScoreTimes5 = getInt((count * 0.7), "floor");
                int ScoreTimes4 = getInt((count * 0.2), "floor");
                int ScoreTimes3 = getInt((count * 0.1), "floor");
                if (ScoreTimes5 == 0)
                    ScoreTimes5 = Convert.ToInt32(count);
                int ScoreTimes = ScoreTimes5 + ScoreTimes4 + ScoreTimes3;
                int ScoreSum = ScoreTimes5 * 5 + ScoreTimes4 * 4 + ScoreTimes3 * 3;
                int ScoreAvg = getInt(((double)ScoreSum * 2 / (double)ScoreTimes), "ceil");
                AppCommentSummaryEntity entity = new AppCommentSummaryEntity() { AppID = appID, CommentTimes = 0, ScoreSum = ScoreSum, ScoreTimes = ScoreTimes, ScoreAvg = ScoreAvg, ScoreTimes1 = 0, ScoreTimes2 = 0, ScoreTimes3 = ScoreTimes3, ScoreTimes4 = ScoreTimes4, ScoreTimes5 = ScoreTimes5 };
                if (Delete(appID))
                {
                    Insert(entity);
                }
            }
        }
        public string [] get_scale(int recommlevel)
        {
            string[] scales = new string[1];
            if (recommlevel < 6)
            {
                scales = new string[]{
                    "[0,0.4,0.3,0.2,0.1]",
                    "[0.05,0.5,0.3,0.05,0.1]",     
                    "[0.1,0.3,0.3,0.1,0.2]",   
                    "[0.05,0.4,0.3,0.05,0.2]",
                    "[0.15,0.4,0.25,0.1,0.1]"
                    };

            }
            else if (recommlevel >= 6 && recommlevel < 7)
            {

                scales = new string[]{
                            "[0.2,0.4,0.2,0.1,0.1]",     
                            "[0.4,0.2,0.2,0.1,0.1]",     
                            "[0.2,0.4,0.3,0.05,0.05]", 
                            "[0.3,0.4,0.2,0.05,0.05]", 
                            "[0.4,0.3,0.2,0.05,0.05]", 
                            "[0.4,0.3,0.2,0.05,0.05]",
                            "[0.3,0.3,0.2,0.1,0.1]",     
                            "[0.2,0.4,0.3,0.05,0.05]", 
                            "[0.3,0.4,0.2,0.05,0.05]", 
                            "[0.2,0.4,0.3,0.05,0.05]",
                            "[0.2,0.4,0.3,0.05,0.05]"
            };
            }
            else if (recommlevel >= 7 && recommlevel < 8)
            {
                scales = new string[]{
                            "[0.4,0.3,0.2,0.05,0.05]",     
                            "[0.5,0.3,0.1,0.1, 0.1]",  
                            "[0.6,0.2,0.1,0.05,0.05]",  
                            "[0.7,0.1,0.1,0.05,0.05]",  
                            "[0.2,0.4,0.3,0.05,0.05]", 
                            "[0.3,0.4,0.2,0.05,0.05]", 
                            "[0.4,0.3,0.2,0.05,0.05]", 
                            "[0.5,0.3,0.1,0.1, 0.1]",  
                            "[0.6,0.2,0.1,0.05,0.05]",   
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.4,0.3,0.2,0.05,0.05]",  
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]",  
                            "[0.2,0.4,0.3,0.05,0.05]", 
                            "[0.3,0.4,0.2,0.05,0.05]", 
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]",  
                            "[0.2,0.4,0.3,0.05,0.05]",  
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]",  
                            "[0.2,0.4,0.3,0.05,0.05]", 
                            "[0.5,0.3,0.1,0.1, 0.1]",  
                            "[0.6,0.2,0.1,0.05,0.05]",   
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.4,0.3,0.2,0.05,0.05]",  
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]" 
                };
            }

            else if (recommlevel >= 8 && recommlevel < 9)
            {
                scales = new string[]{
                            "[0.5,0.3,0.1,0.1, 0.1]",  
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]",   
                            "[0.5,0.3,0.1,0.1, 0.1]",  
                            "[0.6,0.2,0.1,0.05,0.05]",   
                            "[0.7,0.1,0.1,0.05,0.05]",
                            "[0.6,0.2,0.1,0.05,0.05]",  
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]",   
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]",   
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.1,0.1,0.05,0.05]"
                };
            }
            else if (recommlevel >= 9)
            {
                scales = new string[]{
                            "[0.5,0.3,0.1,0.1, 0.1]",  
                            "[0.6,0.2,0.1,0.05,0.05]", 
                            "[0.7,0.15,0.1,0,0.05]",   
                            "[0.5,0.35,0.05,0.05,0.05]",  
                            "[0.6,0.2,0.1,0.05,0.05]",   
                            "[0.7,0.1,0.1,0.05,0.05]", 
                            "[0.65,0.2,0.05,0.05,0.05]",   
                            "[0.55,0.3,0.05,0.05,0.05]", 
                            "[0.7,0.05,0.15,0.05,0.05]",   
                            "[0.6,0.25,0.05,0.05,0.05]", 
                            "[0.7,0.1,0.05,0.1,0.05]",   
                            "[0.8,0.05,0.05,0.05,0.05]", 
                            "[0.7,0.05,0.15,0.05,0.05]"
                };
            }
            Random ran = new Random();
            int index = ran.Next(0, scales.Length - 1);
            string str = scales[index];
            str = str.TrimStart('[');
            str = str.TrimEnd(']');
            string [] a = str.Split(',');
            return a;

        }
        public int getInt(double val, string math)
        {
            //nwbase_utils.TextLog.Default.Info(val.ToString());
            int count = 0;
            if (val.ToString().IndexOf('.') < 0)
            {
                count = Convert.ToInt32(val);
            }
            else
            {
                int a = Convert.ToInt32(val.ToString().Substring(0, val.ToString().IndexOf('.')));
                if (math == "floor")
                {
                    count = a;
                }
                else if (math == "ceil")
                {
                    count = a + 1;
                }

            }
            return count;
        }
    }
}
