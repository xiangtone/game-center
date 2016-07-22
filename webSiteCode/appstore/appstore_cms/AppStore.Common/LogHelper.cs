using log4net;
using log4net.Appender;
using log4net.Config;
using log4net.Core;
using log4net.Layout;
using log4net.Repository;
using System;
using System.IO;

namespace AppStore.Common
{
    public class LogHelper
    {

        public static void Debug(string appenderName, string message)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsDebugEnabled)
            {
                logByName.Debug(message);
            }
        }

        public static void Debug(string appenderName, string message, Exception exception)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsDebugEnabled)
            {
                logByName.Debug(message, exception);
            }
        }

        public static void Error(string appenderName, string message)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsErrorEnabled)
            {
                logByName.Error(message);
            }
        }

        public static void Error(string appenderName, string message, Exception exception)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsErrorEnabled)
            {
                logByName.Error(message, exception);
            }
        }

        public static void Fatal(string appenderName, string message)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsFatalEnabled)
            {
                logByName.Fatal(message);
            }
        }

        public static void Fatal(string appenderName, string message, Exception exception)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsFatalEnabled)
            {
                logByName.Fatal(message, exception);
            }
        }

        public static ILog GetLogByName(string appenderName)
        {
            if (string.IsNullOrEmpty(appenderName))
            {
                throw new ArgumentNullException("日志配置名称不能为空");
            }
            return LogManager.GetLogger(appenderName);
        }

        public static void Info(string appenderName, string message)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsInfoEnabled)
            {
                logByName.Info(message);
            }
        }

        public static void Info(string appenderName, string message, Exception exception)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsInfoEnabled)
            {
                logByName.Info(message, exception);
            }
        }

        public static void LoadConfig(string configFilePath)
        {
            if (string.IsNullOrEmpty(configFilePath) || !File.Exists(configFilePath))
            {
                throw new ArgumentNullException("日志配置文件不存在");
            }
            XmlConfigurator.ConfigureAndWatch(new FileInfo(configFilePath));
        }

        public static void Warn(string appenderName, string message)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsWarnEnabled)
            {
                logByName.Warn(message);
            }
        }

        public static void Warn(string appenderName, string message, Exception exception)
        {
            ILog logByName = GetLogByName(appenderName);
            if (logByName.IsWarnEnabled)
            {
                logByName.Warn(message, exception);
            }
        }

        public static class Default
        {
            private static Config _DebugConfig;
            private static ILog _DebugLogger;
            private static Config _ErrorConfig;
            private static ILog _ErrorLogger;
            private static Config _FatalConfig;
            private static ILog _FatalLogger;
            private static Config _InfoConfig;
            private static ILog _InfoLogger;
            private static Config _WarnConfig;
            private static ILog _WarnLogger;

            public static void Debug(string message)
            {
                DebugLogger.Debug(message);
            }

            public static void Error(string message)
            {
                ErrorLogger.Error(message);
            }

            public static void Fatal(string message)
            {
                FatalLogger.Fatal(message);
            }

            public static void Info(string message)
            {
                InfoLogger.Info(message);
            }

            private static ILog LoadConf(Config config)
            {
                PatternLayout layout = new PatternLayout(config.PatternLayout);
                AppenderSkeleton appender = config.Appender as TextWriterAppender;
                appender.Layout = layout;
                appender.ActivateOptions();
                ILoggerRepository repository = LogManager.CreateRepository(Guid.NewGuid().ToString());
                BasicConfigurator.Configure(repository, appender);
                return LogManager.GetLogger(repository.Name, Guid.NewGuid().ToString());
            }

            public static void Warn(string message)
            {
                WarnLogger.Warn(message);
            }

            private static Config DebugConfig
            {
                get
                {
                    if (_DebugConfig == null)
                    {
                        _DebugConfig = new Config(Level.Debug);
                    }
                    return _DebugConfig;
                }
            }

            private static ILog DebugLogger
            {
                get
                {
                    if (_DebugLogger == null)
                    {
                        _DebugLogger = LoadConf(DebugConfig);
                    }
                    return _DebugLogger;
                }
            }

            private static Config ErrorConfig
            {
                get
                {
                    if (_ErrorConfig == null)
                    {
                        _ErrorConfig = new Config(Level.Error);
                    }
                    return _ErrorConfig;
                }
            }

            private static ILog ErrorLogger
            {
                get
                {
                    if (_ErrorLogger == null)
                    {
                        _ErrorLogger = LoadConf(ErrorConfig);
                    }
                    return _ErrorLogger;
                }
            }

            private static Config FatalConfig
            {
                get
                {
                    if (_FatalConfig == null)
                    {
                        _FatalConfig = new Config(Level.Fatal);
                    }
                    return _FatalConfig;
                }
            }

            private static ILog FatalLogger
            {
                get
                {
                    if (_FatalLogger == null)
                    {
                        _FatalLogger = LoadConf(FatalConfig);
                    }
                    return _FatalLogger;
                }
            }

            private static Config InfoConfig
            {
                get
                {
                    if (_InfoConfig == null)
                    {
                        _InfoConfig = new Config(Level.Info);
                    }
                    return _InfoConfig;
                }
            }

            private static ILog InfoLogger
            {
                get
                {
                    if (_InfoLogger == null)
                    {
                        _InfoLogger = LoadConf(InfoConfig);
                    }
                    return _InfoLogger;
                }
            }

            private static Config WarnConfig
            {
                get
                {
                    if (_WarnConfig == null)
                    {
                        _WarnConfig = new Config(Level.Warn);
                    }
                    return _WarnConfig;
                }
            }

            private static ILog WarnLogger
            {
                get
                {
                    if (_WarnLogger == null)
                    {
                        _WarnLogger = LoadConf(WarnConfig);
                    }
                    return _WarnLogger;
                }
            }

            internal class Config
            {
                private IAppender _Appender;
                public string PatternLayout;

                public Config(IAppender appender)
                {
                    this.PatternLayout = "%d{yyyy-MM-dd HH:mm:ss}:%m%n";
                    this._Appender = appender;
                }

                public Config(Level level)
                {
                    this.PatternLayout = "%d{yyyy-MM-dd HH:mm:ss}       %m%n";
                    RollingFileAppender appender = new RollingFileAppender
                    {
                        RollingStyle = RollingFileAppender.RollingMode.Date,
                        DatePattern = "yyyyMMdd\".txt\"",
                        StaticLogFileName = false,
                        AppendToFile = true,
                        File = @"LOG\" + level.ToString() + @"\",
                        ImmediateFlush = true,
                        LockingModel = new FileAppender.MinimalLock()
                    };
                    this._Appender = appender;
                }

                public IAppender Appender
                {
                    get
                    {
                        return this._Appender;
                    }
                }
            }
        }
    }
}
