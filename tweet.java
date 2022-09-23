import java.util.ArrayList;
import java.util.List;

public class tweet {
    private String user;
    public String text;
    private String date;
    public boolean sentiment;

    public ArrayList<String> words;

    public tweet(String user, String text, String date, boolean sentiment){
        this.user = user;
        this.text = text;
        this.date = date;
        this.sentiment = sentiment;
        this.words = new ArrayList<String>();
        text = clean(text);
        while(text.indexOf(" ") > -1){
            String word = text.substring(0, text.indexOf(" "));
            if(!word.equals("") && !word.equals(" ")){
                this.words.add(word);
            }
            text = text.substring(text.indexOf(" ") + 1);
        }
        words.add(text);
    }

    public String toString(){
        return this.user + " tweets on " + this.date + " with sentiment " + this.sentiment + ": " + this.text;
    }

    public void removeStopWords(List<String> STOPWORDS){
        for(int i = 0; i<this.words.size(); i++){
            for(int j = 0; j<STOPWORDS.size(); j++){
                if(this.words.get(i).equals(STOPWORDS.get(j))){
                    this.words.remove(i);
                    i--;
                    break;
                }
            }
        }
    }


    //this method will remove non-alphabet characters from a String
    private static String clean(String word) {
        return word.toLowerCase().replaceAll("[!“”\"\\#$%&()*+,./:;<=>?@\\[\\\\\\]^_‘{|}~]", "" );
    }

    public static ArrayList<ArrayList<String>> allTweetsForUser(ArrayList<tweet> tweets, String user){
        ArrayList<ArrayList<String>> allTweets = new ArrayList<ArrayList<String>>();
        for(int i = 0; i<tweets.size(); i++){
            if(tweets.get(i).user.equals(user)){
                allTweets.add(tweets.get(i).words);
            }
        }
        return allTweets;
    }

    public static ArrayList<String> allWordsForUser(ArrayList<tweet> tweets, String user, boolean unique){
        ArrayList<String> allWords = new ArrayList<String>();
        ArrayList<ArrayList<String>> allTweets = allTweetsForUser(tweets, user);
        for(int i = 0; i<allTweets.size(); i++){
            for(int j = 0; j<allTweets.get(i).size(); j++){
                if(!unique || !allWords.contains(allTweets.get(i).get(j))){
                    allWords.add(allTweets.get(i).get(j));
                }
            }
        }

        return allWords;
    }

    public static void main(String[] args) {
        /*
        ArrayList<tweet> tweets = new ArrayList<tweet>();
        tweets.add(new tweet("john", "john's first tweet", "january 1"));
        tweets.add(new tweet("john", "john's second tweet", "january 1"));
        tweets.add(new tweet("jack", "jack's first tweet", "january 1"));
        tweets.add(new tweet("jack", "jack's second tweet", "january 1"));
        
        System.out.println(allTweetsForUser(tweets, "john"));
        System.out.println(allTweetsForUser(tweets, "jack"));

        System.out.println(allWordsForUser(tweets, "john", true));
        System.out.println(allWordsForUser(tweets, "john", false));
        System.out.println(allWordsForUser(tweets, "jack", true));
        System.out.println(allWordsForUser(tweets, "jack", false));

        System.out.println(new tweet("isabel", "hi!", "january 1", true));
        */

    
    }
}
