package util;

/**
 * 异常处理类 - 统一处理系统中的异常
 * 提供标准化的异常和验证错误处理方法
 */
public class ExceptionHandler {

    // 私有构造函数，防止实例化
    private ExceptionHandler() {}

    /**
     * 处理异常 - 打印错误信息和堆栈跟踪
     * @param e 要处理的异常对象
     */
    public static void handleException(Exception e) {
        System.err.println("[ERROR] " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * 处理验证异常 - 打印验证错误信息
     * @param message 验证错误消息
     */
    public static void handleValidationException(String message) {
        System.err.println("[VALIDATION_ERROR] " + message);
    }
}