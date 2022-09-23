import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

/**
 * This class will handle loading the data from our .csv file
 * It should create an ArrayList of Tweet objects
 * Eventually we will analyize data from the tweets and use that
 * to make predictions about other tweets/text.
 */
public class DataProcessor
{
    //don't worry about this right now, but we will be using it later
    public static final List<String> STOPWORDS = Arrays.asList(new String[]{"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"});
    private ArrayList<tweet> tweets;
    private ArrayList<wordPosNeg> database;
    private int totalPos;
    private int totalNeg;

    //load the file given by filename and use it to create
    //an arraylist of tweets
    public DataProcessor(String filename)
    {
        this.tweets = new ArrayList<tweet>();
        this.database = new ArrayList<wordPosNeg>();
        Scanner s = null;
        try {
            s = new Scanner(new File(filename));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        while (s.hasNextLine())
        {
            String data = s.nextLine();
            //String idNumber = data.substring(0, data.indexOf(","));
            data = data.substring(data.indexOf(",") + 1);
            String sentNum = data.substring(0, data.indexOf(","));
            boolean sentiment;
            sentiment = sentNum.equals("4");
            data = data.substring(data.indexOf(",") + 1);
            //String tweetNumber = data.substring(0, data.indexOf(","));
            data = data.substring(data.indexOf(",") + 1);
            String date = data.substring(0, data.indexOf(","));
            data = data.substring(data.indexOf(",") + 1);
            String user = data.substring(0, data.indexOf(","));
            data = data.substring(data.indexOf(",") + 1);
            String text = data;
            tweets.add(new tweet(user, text, date, sentiment));
        }

        for(int i = 0; i<tweets.size(); i++){
            tweets.get(i).removeStopWords(STOPWORDS);
            ArrayList<String> goWords = tweets.get(i).words;
            //System.out.println(goWords);

            if(tweets.get(i).sentiment){
                totalPos++;
            }
            else{
                totalNeg++;
            }


            for(int j = 0; j<goWords.size(); j++){
                boolean counted = false;
                for(int k = 0; k<database.size() && !counted; k++){
                    if(database.get(k).word.equals(goWords.get(j))){
                        database.get(k).total++;
                        if(tweets.get(i).sentiment){
                            database.get(k).pos++;
                        }
                        else{
                            database.get(k).neg++;
                        }
                        counted = true;
                    }
                }

                if(!counted){
                    if(tweets.get(i).sentiment){
                        database.add(new wordPosNeg(goWords.get(j), 3, 2, 1));
                    }
                    else{
                        database.add(new wordPosNeg(goWords.get(j), 3, 1, 2));
                    }
                }
            }
        }
    }


    public boolean predict(String text){

        double positiveSentiment = (double) totalPos / (double) (totalPos + totalNeg);
        double negativeSentiment = (double) totalNeg / (double) (totalPos + totalNeg);

        //System.out.println(positiveSentiment + " " + negativeSentiment);
        
        tweet t = new tweet("", text, "", true);
        t.removeStopWords(STOPWORDS);
        ArrayList<String> words = t.words;

        //System.out.println(words);

        for(int i = 0; i<words.size(); i++){
            String word = words.get(i);
            for(int j = 0; j<database.size(); j++){
                if(database.get(j).word.equals(word)){
                    positiveSentiment *= (double) (database.get(j).pos / (double)(totalPos+1));
                    negativeSentiment *= (double) (database.get(j).neg / (double)(totalNeg+1));

                    //System.out.println((double)(database.get(j).pos / (double)(totalPos+1)) + " " + (double)(database.get(j).neg / (double)(totalNeg+1)));
                    break;
                }
            }
        }

        //System.out.println(positiveSentiment + " " + negativeSentiment);

        return positiveSentiment > negativeSentiment;
    }
    
    
    
    
    public static void main(String [] args)
    {
        DataProcessor processor = new DataProcessor("SmallData.csv");
        /*
        for(int i = 0; i<processor.tweets.size(); i++){
            System.out.println(processor.tweets.get(i));
            System.out.println(processor.tweets.get(i).words);
        }
        */

        /*
        for(int i = 0; i<processor.database.size(); i++){
            System.out.println(processor.database.get(i));
        }
        */
        
        
        //System.out.println(processor.totalPos + " " + processor.totalNeg);

        //System.out.println(processor.predict("work"));
        //System.out.println(processor.predict("work lt3"));

        DataProcessor bigProcessor = new DataProcessor("data.csv");


        int correct = 0;
        for(int i = 0; i<bigProcessor.tweets.size(); i++){
            boolean prediction = bigProcessor.predict(bigProcessor.tweets.get(i).text);
            if(prediction == bigProcessor.tweets.get(i).sentiment){
                correct++;
            }
            else{
                System.out.println(bigProcessor.tweets.get(i).text);
            }
        }

        System.out.println(correct + " out of 2000: " + 100*(double) correct/2000 + "% accuracy");
        //1927 out of 2000: 96.25% accuracy



        //using SmallData as testing data
        correct = 0;
        for(int i = 0; i<processor.tweets.size(); i++){
            boolean prediction = bigProcessor.predict(processor.tweets.get(i).text);
            if(prediction == processor.tweets.get(i).sentiment){
                correct++;
            }
            else{
                System.out.println(bigProcessor.tweets.get(i).text);
            }
        }

        System.out.println(correct + " out of 20: " + 100*(double) correct/20 + "% accuracy");
        //20 out of 20: 100.0% accuracy

        
    }
}
