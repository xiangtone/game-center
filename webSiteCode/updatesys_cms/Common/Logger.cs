using log4net;
using log4net.Appender;
using log4net.Config;
using log4net.Core;
using log4net.Layout;
using log4net.Repository;
using System;
using System.IO;

namespace Common
{
    public class Logger
    {
        public static void WriteLog(string message)
        {
            TextLog.Error("error", message);
            //using (FileStream fs = new FileStream(HttpContext.Current.Server.MapPath("~/log.log"), FileMode.Append, FileAccess.Write))
            //{
            //    using (StreamWriter sw = new StreamWriter(fs, Encoding.UTF8))
            //    {
            //        sw.WriteLine(DateTime.Now.ToString() + "：" + message);
            //        sw.Close();
            //    }
            //    fs.Close();
            //}
        }
    }

    /// 版权所有: 版权所有(C) 2012
    /// 内容摘要:文本文件日志
    /// 完成日期：2012年05月07日
    /// 版    本：V2.0 
    /// 作    者：王道军
    ///
    /// 修改记录1: 
    /// 修改日期： 
    /// 版 本 号：
    /// 修 改 人： 
    /// 修改内容：
    public static class TextLog
    {
        /// <summary>
        /// 使用默认格式的日志
        /// </summary>
        public static class Default
        {
            internal class Config
            {
                private IAppender _Appender;
                public string PatternLayout = "%d{yyyy-MM-dd HH:mm:ss}:%m%n";
                public IAppender Appender
                {
                    get
                    {
                        return this._Appender;
                    }
                }
                public Config(Level level)
                {
                    this._Appender = new RollingFileAppender
                    {
                        RollingStyle = RollingFileAppender.RollingMode.Date,
                        DatePattern = "yyyyMMdd\".txt\"",
                        StaticLogFileName = false,
                        AppendToFile = true,
                        File = "LOG\\" + level.ToString() + "\\log_",
                        ImmediateFlush = true,
                        LockingModel = new FileAppender.MinimalLock()
                    };
                }
                public Config(IAppender appender)
                {
                    this._Appender = appender;
                }
            }
            private static ILog _InfoLogger;
            private static ILog _ErrorLogger;
            private static ILog _DebugLogger;
            private static ILog _WarnLogger;
            private static ILog _FatalLogger;
            private static TextLog.Default.Config _InfoConfig;
            private static TextLog.Default.Config _DebugConfig;
            private static TextLog.Default.Config _WarnConfig;
            private static TextLog.Default.Config _ErrorConfig;
            private static TextLog.Default.Config _FatalConfig;
            private static ILog InfoLogger
            {
                get
                {
                    if (TextLog.Default._InfoLogger != null)
                    {
                        return TextLog.Default._InfoLogger;
                    }
                    TextLog.Default._InfoLogger = TextLog.Default.LoadConf(TextLog.Default.InfoConfig);
                    return TextLog.Default._InfoLogger;
                }
            }
            private static ILog ErrorLogger
            {
                get
                {
                    if (TextLog.Default._ErrorLogger != null)
                    {
                        return TextLog.Default._ErrorLogger;
                    }
                    TextLog.Default._ErrorLogger = TextLog.Default.LoadConf(TextLog.Default.ErrorConfig);
                    return TextLog.Default._ErrorLogger;
                }
            }
            private static ILog DebugLogger
            {
                get
                {
                    if (TextLog.Default._DebugLogger != null)
                    {
                        return TextLog.Default._DebugLogger;
                    }
                    TextLog.Default._DebugLogger = TextLog.Default.LoadConf(TextLog.Default.DebugConfig);
                    return TextLog.Default._DebugLogger;
                }
            }
            private static ILog WarnLogger
            {
                get
                {
                    if (TextLog.Default._WarnLogger != null)
                    {
                        return TextLog.Default._WarnLogger;
                    }
                    TextLog.Default._WarnLogger = TextLog.Default.LoadConf(TextLog.Default.WarnConfig);
                    return TextLog.Default._WarnLogger;
                }
            }
            private static ILog FatalLogger
            {
                get
                {
                    if (TextLog.Default._FatalLogger != null)
                    {
                        return TextLog.Default._FatalLogger;
                    }
                    TextLog.Default._FatalLogger = TextLog.Default.LoadConf(TextLog.Default.FatalConfig);
                    return TextLog.Default._FatalLogger;
                }
            }
            private static TextLog.Default.Config InfoConfig
            {
                get
                {
                    if (TextLog.Default._InfoConfig == null)
                    {
                        TextLog.Default._InfoConfig = new TextLog.Default.Config(Level.Info);
                    }
                    return TextLog.Default._InfoConfig;
                }
            }
            private static TextLog.Default.Config DebugConfig
            {
                get
                {
                    if (TextLog.Default._DebugConfig == null)
                    {
                        TextLog.Default._DebugConfig = new TextLog.Default.Config(Level.Debug);
                    }
                    return TextLog.Default._DebugConfig;
                }
            }
            private static TextLog.Default.Config WarnConfig
            {
                get
                {
                    if (TextLog.Default._WarnConfig == null)
                    {
                        TextLog.Default._WarnConfig = new TextLog.Default.Config(Level.Warn);
                    }
                    return TextLog.Default._WarnConfig;
                }
            }
            private static TextLog.Default.Config ErrorConfig
            {
                get
                {
                    if (TextLog.Default._ErrorConfig == null)
                    {
                        TextLog.Default._ErrorConfig = new TextLog.Default.Config(Level.Error);
                    }
                    return TextLog.Default._ErrorConfig;
                }
            }
            private static TextLog.Default.Config FatalConfig
            {
                get
                {
                    if (TextLog.Default._FatalConfig == null)
                    {
                        TextLog.Default._FatalConfig = new TextLog.Default.Config(Level.Fatal);
                    }
                    return TextLog.Default._FatalConfig;
                }
            }
            static Default()
            {
            }
            private static ILog LoadConf(TextLog.Default.Config config)
            {
                PatternLayout layout = new PatternLayout(config.PatternLayout);
                AppenderSkeleton appenderSkeleton = config.Appender as TextWriterAppender;
                appenderSkeleton.Layout = layout;
                appenderSkeleton.ActivateOptions();
                ILoggerRepository loggerRepository = LogManager.CreateRepository(Guid.NewGuid().ToString());
                BasicConfigurator.Configure(loggerRepository, appenderSkeleton);
                return LogManager.GetLogger(loggerRepository.Name, Guid.NewGuid().ToString());
            }
            /// <summary>
            /// 记录信息日志
            /// </summary>
            public static void Info(string message)
            {
                TextLog.Default.InfoLogger.Info(message);
            }
            /// <summary>
            /// 记录调试日志
            /// </summary>
            public static void Debug(string message)
            {
                TextLog.Default.DebugLogger.Debug(message);
            }
            /// <summary>
            /// 记录警告日志
            /// </summary>
            public static void Warn(string message)
            {
                TextLog.Default.WarnLogger.Warn(message);
            }
            /// <summary>
            /// 记录错误日志
            /// </summary>
            public static void Error(string message)
            {
                TextLog.Default.ErrorLogger.Error(message);
            }
            /// <summary>
            /// 记录严重错误日志
            /// </summary>
            public static void Fatal(string message)
            {
                TextLog.Default.FatalLogger.Fatal(message);
            }
        }
        /// <summary>
        /// 加载log4net配置文件
        /// </summary>
        /// <param name="configFilePath">配置文件路径</param>
        public static void LoadConfig(string configFilePath)
        {
            if (string.IsNullOrEmpty(configFilePath) || !File.Exists(configFilePath))
            {
                throw new ArgumentNullException("日志配置文件不存在");
            }
            XmlConfigurator.ConfigureAndWatch(new FileInfo(configFilePath));
        }
        /// <summary>
        /// 记录调试日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        public static void Debug(string appenderName, string message)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsDebugEnabled)
            {
                logByName.Debug(message);
            }
        }
        /// <summary>
        /// 记录调试日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        /// <param name="exception">为日志附加异常信息，可以在配置中获取更准确的信息，如无异常信息，则可以为null</param>
        public static void Debug(string appenderName, string message, Exception exception)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsDebugEnabled)
            {
                logByName.Debug(message, exception);
            }
        }
        /// <summary>
        /// 记录信息日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        public static void Info(string appenderName, string message)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsInfoEnabled)
            {
                logByName.Info(message);
            }
        }
        /// <summary>
        /// 记录信息日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        /// <param name="exception">为日志附加异常信息，可以在配置中获取更准确的信息，如无异常信息，则可以为null</param>
        public static void Info(string appenderName, string message, Exception exception)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsInfoEnabled)
            {
                logByName.Info(message, exception);
            }
        }
        /// <summary>
        /// 记录警告日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        public static void Warn(string appenderName, string message)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsWarnEnabled)
            {
                logByName.Warn(message);
            }
        }
        /// <summary>
        /// 记录警告日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        /// <param name="exception">为日志附加异常信息，可以在配置中获取更准确的信息，如无异常信息，则可以为null</param>
        public static void Warn(string appenderName, string message, Exception exception)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsWarnEnabled)
            {
                logByName.Warn(message, exception);
            }
        }
        /// <summary>
        /// 记录错误日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        public static void Error(string appenderName, string message)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsErrorEnabled)
            {
                logByName.Error(message);
            }
        }
        /// <summary>
        /// 记录错误日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        /// <param name="exception">为日志附加异常信息，可以在配置中获取更准确的信息，如无异常信息，则可以为null</param>
        public static void Error(string appenderName, string message, Exception exception)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsErrorEnabled)
            {
                logByName.Error(message, exception);
            }
        }
        /// <summary>
        /// 记录严重错误日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        public static void Fatal(string appenderName, string message)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsFatalEnabled)
            {
                logByName.Fatal(message);
            }
        }
        /// <summary>
        /// 记录严重错误日志
        /// </summary>
        /// <param name="appenderName">日志记录员，不同的记录员可能写入不同的日志文件（在配置文件中配置）</param>
        /// <param name="message">日志信息，如果因异常记录日志，则message可以为null</param>
        /// <param name="exception">为日志附加异常信息，可以在配置中获取更准确的信息，如无异常信息，则可以为null</param>
        public static void Fatal(string appenderName, string message, Exception exception)
        {
            ILog logByName = TextLog.GetLogByName(appenderName);
            if (logByName.IsFatalEnabled)
            {
                logByName.Fatal(message, exception);
            }
        }
        /// <summary>
        /// 根据指定的日志名称获取日志对象
        /// </summary>
        /// <param name="appenderName">日志名称</param>
        /// <returns>日志对象</returns>
        public static ILog GetLogByName(string appenderName)
        {
            if (string.IsNullOrEmpty(appenderName))
            {
                throw new ArgumentNullException("日志配置名称不能为空");
            }
            return LogManager.GetLogger(appenderName);
        }
    }
}