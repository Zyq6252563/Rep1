package util;

/**
 * 加密工具类 - 提供密码加密功能
 * 使用简单的哈希算法对密码进行加密
 */
public class EncryptionUtil {

    // 私有构造函数，防止实例化
    private EncryptionUtil() {}

    /**
     * 对密码进行哈希加密
     * 使用简单的哈希函数（仅使用Java基础库）
     * 这是一个基本实现，不使用java.security包
     * 它从密码生成确定性的哈希字符串
     *
     * @param password 要加密的密码
     * @return 64位十六进制字符串哈希值
     * @throws IllegalArgumentException 如果密码为null
     */
    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        // 使用基本字符串和数学运算的简单哈希算法
        // 这创建了一个类似于SHA-256输出的64字符十六进制字符串
        long hash = 7;
        StringBuilder result = new StringBuilder();

        // 使用简单算术生成哈希值
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            hash = hash * 31 + c;
            hash = hash ^ (hash >>> 16);
        }

        // 生成64个十六进制字符以模拟SHA-256格式
        long currentHash = hash;
        for (int i = 0; i < 16; i++) {
            // 每次迭代生成4个十六进制数字
            currentHash = currentHash * 1103515245 + 12345;
            // 使用按位与确保正值（0-65535范围）
            long segment = ((currentHash / 65536) & 0xFFFF);
            String hexSegment = Long.toHexString(segment);

            // 如果需要，用零填充
            while (hexSegment.length() < 4) {
                hexSegment = "0" + hexSegment;
            }

            result.append(hexSegment);
        }

        return result.toString();
    }
}
