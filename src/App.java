import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int length;

        System.out.print("Input Length: ");
        length = sc.nextInt();
        System.out.println();

        WordCount wordCount = new WordCount(length);
        CharacterCount characterCount = new CharacterCount(length);

        Runnable r1 = () -> {
            System.out.println("Total words: " + wordCount.count);
            System.out.println();
        };
        Runnable r2 = () -> {
            System.out.print("Total character: " + characterCount.count);
            System.out.println();
        };

        CyclicBarrier barrier1 = new CyclicBarrier(1, r1);
        CyclicBarrier barrier2 = new CyclicBarrier(1, r2);

        Main main = new Main(barrier1, barrier2, wordCount, characterCount);
        new Thread(main).start();
    }
}

class Main implements Runnable {
    WordCount wordCount;
    CharacterCount characterCount;
    CyclicBarrier barrier1, barrier2;

    public Main(CyclicBarrier barrier1, CyclicBarrier barrier2, WordCount wordCount, CharacterCount characterCount) {
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
        this.wordCount = wordCount;
        this.characterCount = characterCount;
    }

    public void run() {
        try {
            double start, end, executionTime;
            start = System.currentTimeMillis();

            wordCount.run();
            this.barrier1.await();

            characterCount.run();
            this.barrier2.await();

            end = System.currentTimeMillis();
            executionTime = (end - start) / 1000;
            System.out.printf("%n%s%.3f%s%n", "Execution time: ", executionTime, " seconds");

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}



class WordCount implements Runnable {
    public int length;
    int count;
    List<String> list = new ArrayList<>();
    List<String> newList = new ArrayList<>();

    public WordCount(int length) {
        this.length = length;
    }

    public void run() {
        try {
            wordCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void wordCount() throws IOException {
        File file = new File("src/RossBeresford.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String[] words;
        String str;

        while ((str = br.readLine()) != null) {
            str = str.replaceAll("[(),.'’]", "");
            words = str.split(" ");
            for (String wordSearches : words) {
                int wordSearchesLength = wordSearches.length();
                if (wordSearchesLength == length) {
                    list.add(wordSearches);

                }
            }
        }
        newList = list.stream()
                .distinct()
                .collect(Collectors.toList());
        for (String wordSearches : newList) {
            System.out.println(wordSearches);
            count++;
        }
        fr.close();
    }
}

class CharacterCount implements Runnable {
    public int length;
    int count;
    List<String> list = new ArrayList<>();
    List<String> newList = new ArrayList<>();

    public CharacterCount(int length) {
        this.length = length;
    }

    public void run() {
        try {
            characterCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void characterCount() throws IOException {
        File file = new File("src/RossBeresford.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String[] words;

        String str;

        while ((str = br.readLine()) != null) {
            str = str.replaceAll("[(),.'’]", "");
            words = str.split(" ");
            for (String wordSearches : words) {
                int wordSearchesLength = wordSearches.length();
                if (wordSearchesLength == length) {
                    list.add(wordSearches);
                }
            }
        }
        newList = list.stream()
                .distinct()
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (String wordSearches : newList) {
            sb.append(wordSearches);
        }
        List<Character> charlist = new ArrayList<>();
        List<Character> newcharList;
        char[] ch = sb.toString().toCharArray();
        for (char chSearches : ch) {
            charlist.add(chSearches);
        }
        newcharList = charlist.stream()
                .distinct()
                .collect(Collectors.toList());
        for (int i = 0; i < newcharList.size(); i++) {
            char c = newcharList.get(i);
            if (i == 0)
                System.out.print(c);
            else
                System.out.print(", " + c);
            count++;
        }
        System.out.println();
        fr.close();
    }
}