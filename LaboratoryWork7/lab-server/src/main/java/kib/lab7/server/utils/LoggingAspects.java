    package kib.lab7.server.utils;

    import kib.lab7.common.util.client_server_communication.requests.CommandRequest;
    import org.apache.logging.log4j.LogManager;
    import org.apache.logging.log4j.Logger;
    import org.aspectj.lang.JoinPoint;
    import org.aspectj.lang.annotation.AfterReturning;
    import org.aspectj.lang.annotation.AfterThrowing;
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.Before;

    import java.util.Arrays;

    @Aspect
    public class LoggingAspects {

        private static final Logger LOGGER = LogManager.getLogger();

        @Before("execution(void launch*())")
        public void launchingMethodsAdvises(JoinPoint joinPoint) {
            LOGGER.info("Вызван метод: " + joinPoint.getSignature().getName());
        }

        @AfterReturning(pointcut = "execution(boolean fillCollection())", returning = "result")
        public void collectionFillingAdvise(Object result) {
            if ((Boolean) result) {
                LOGGER.info("Коллекция успешно заполнена из файла");
            } else {
                LOGGER.info("Коллекция не была заполнена, сервер не запустился");
            }

        }

        @AfterReturning(pointcut = "execution(kib.lab7.common.util.client_server_communication.requests.CommandRequest listen())", returning = "result")
        public void requestListeningAdvise(Object result) {
            if (result != null) {
                LOGGER.info("Получен запрос от " + ((CommandRequest) result).getClientInfo() + " --> " + result);
            }
        }

        @AfterReturning(pointcut = "execution(Object executeCommandFromRequest(kib.lab7.common.util.client_server_communication.requests.CommandRequest))", returning = "result")
        public void commandExecutionAdvise(JoinPoint joinPoint, Object result) {
            if (result != null) {
                LOGGER.info("Исполнена команда: "
                        + ((CommandRequest) joinPoint.getArgs()[0]).getCommandNameToSend());
            }
        }

        @AfterReturning(pointcut = "execution(String sendResponse(kib.lab7.common.util.client_server_communication.responses.CommandResponse))", returning = "result")
        public void sendResponseAdvise(Object result) {
            LOGGER.info("Ответ отправлен клиенту " + result);
        }

        @AfterReturning(pointcut = "execution(public * kib.lab7.server.utils.CollectionManager.*(..))")
        public void collectionManagerAnyMethodAdvise(JoinPoint joinPoint) {
            LOGGER.info("Обращение к менеджеру коллекции: " + joinPoint.getSignature().getName()
                    + " / Аргументы: " + Arrays.toString(joinPoint.getArgs()));
        }

        @AfterReturning(pointcut = "execution(public * kib.lab7.server.db_utils.DBManager.*(..))")
        public void dbManagerAnyMethodAdvice(JoinPoint joinPoint) {
            LOGGER.info("Обращение к менеджеру базы данных: "
            + joinPoint.getSignature().getName());
        }

        //Необходимо для логирования критических ошибок работы приложения
        @AfterThrowing(pointcut = "execution(* *(..))", throwing = "ex")
        public void afterAnyThrowingAdvise(Throwable ex) {
            LOGGER.error("Возникла критическая ошибка, сервер прекратил свою работу: \n"
                    + Arrays.toString(ex.getStackTrace()));
        }
    }
