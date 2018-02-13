import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class getsynonym
{
    public ArrayList<String> generate(String wordForm)
    {
        File f=new File(".\\WordNet\\2.1\\dict");
        System.setProperty("wordnet.database.dir", f.toString());
        //setting path for the WordNet Directory

        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(wordForm);
        ArrayList<String> al = new ArrayList<String>();
        if (synsets.length > 0){

            // add elements to al, including duplicates
            HashSet hs = new HashSet();

            for (int i = 0; i < synsets.length; i++){

                String[] wordForms = synsets[i].getWordForms();
                for (int j = 0; j < wordForms.length; j++)
                {
                    al.add(wordForms[j]);
                }


                //removing duplicates
                hs.addAll(al);
                al.clear();
                al.addAll(hs);
            }
        }
        return al;
    }

    public void get(String filename)
    {
        String line,finalstring;
        HashMap<String,ArrayList<String>> syns=new HashMap<>();
        try
        {
            //reading keys from the file and getting their synonyms
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> synlist=new ArrayList<>();
            while ((line=bufferedReader.readLine())!=null)
            {
                synlist=generate(line);
                syns.put(line,synlist);
            }
            bufferedReader.close();
            //Writing back into the file with synonyms
            FileWriter fileWriter = new FileWriter(filename);
            BufferedWriter b=new BufferedWriter(fileWriter);
            for (String word:syns.keySet())
            {
                synlist=syns.get(word);
                finalstring=word+" - ";
                for (int i=0;i<synlist.size()-1;i++)
                {
                    String part=synlist.get(i);
                    finalstring+=part+" , ";
                }
                finalstring+=synlist.get(synlist.size()-1);
                b.write(finalstring);
                b.newLine();
            }
            b.close();
        }
        catch(IOException e)
        {
            System.out.println("Couldn't find file");
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            try
            {
                FileWriter fileWriter = new FileWriter(filename);
                BufferedWriter b = new BufferedWriter(fileWriter);
                for (String i : syns.keySet())
                {

                    b.write(i);
                    b.newLine();
                }
                b.close();
            }
            catch(IOException ex)
            {
                System.out.println("Couldn't find file");
            }
        }

    }

}

