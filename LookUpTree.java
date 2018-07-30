package snippet;

public class LookUpTree {


    public static void main(String[] args) {
        addIp("126.34.65.78");
        addIp("126.35.65.78");

        System.out.println(isPresent("126.34.65.78"));
        System.out.println(isPresent("125.45.24.23"));
    }

    static LookUpNode rootNode = new LookUpNode();

    public static boolean addIp(String ip) {

        byte[] bytes = convertStringIpToBytes(ip);
        return put(bytes, rootNode, 0);
    }

    public static boolean put(byte[] bytes, LookUpNode node, int level) {

        boolean result;
        if (node.size == maxLevelSize(level)) {
            return false;
        }
        byte b = bytes[level];

        if (level == 3) {

            if (node.leaves == null) {
                node.leaves = new boolean[256];
            }

            if (node.leaves[b]) {
                return false;
            } else {
                node.leaves[b] = true;
                node.size++;
                return true;
            }

        } else {

            if (node.children == null) {
                node.children = new LookUpNode[256];
            }

            LookUpNode child = node.children[b];
            if (child == null) {
                child = new LookUpNode();
                node.children[b] = child;
            }
            result = put(bytes, child, level + 1);
            if (result) node.size++;
        }
        return result;
    }

    public static boolean isPresent(String ip) {

        byte[] bytes = convertStringIpToBytes(ip);
        return isExist(bytes, rootNode, 0);
    }

    private static boolean isExist(byte[] bytes, LookUpNode node, int level) {

        boolean result;
        if (node.size == maxLevelSize(level)) {
            return true;
        }

        byte b = bytes[level];
        if (level == 3) {

            if (node.leaves == null || !node.leaves[b]) {
                return false;
            }
            return true;

        } else {

            if (node.children == null || node.children[b] == null) {
                result = false;
            } else {
                result = isExist(bytes, node.children[b], level + 1);
            }
        }
        return result;
    }

    private static long maxLevelSize(int level) {

        long res = 0;
        switch (level) {
            case 0:
                res = 4294967294L;
                break;
            case 1:
                res = 16777214L;
                break;
            case 2:
                res = 65534L;
                break;
            case 3:
                res = 254L;
                break;
        }
        return res;
    }

    private static byte[] convertStringIpToBytes(String ip) {
        String[] bytesStr = ip.split("\\.");
        byte[] bytes = new byte[bytesStr.length];
        for (int i = 0; i < bytesStr.length; i++) {
            bytes[i] = Byte.parseByte(bytesStr[i]);
        }
        return bytes;
    }
}

class LookUpNode {

    int size;
    LookUpNode[] children;
    boolean[] leaves;
}
