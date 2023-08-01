public class Program {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>(4);

        String res = hashMap.put("+7888", "Andrey");
        res = hashMap.put("+7887", "Alex");
        res = hashMap.put("+7887", "Daria1");
        res = hashMap.put("+7888", "Daria2");
        res = hashMap.put("+7881", "Daria3");
        res = hashMap.put("+7882", "Daria4");
        res = hashMap.put("+7883", "Daria5");
        res = hashMap.put("+7884", "Daria6");
        res = hashMap.put("+7885", "Daria7");
        res = hashMap.put("+7886", "Daria8");


        res = hashMap.put("A", "1");
        res = hashMap.put("B", "2");
        res = hashMap.put("C", "3");

        for (HashMap.Entity element : hashMap) {
            System.out.println(element.key + ": " + element.value);
        }


    }
}
