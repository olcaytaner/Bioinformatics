package bioinformatics.util;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * User: root
 * Date: Nov 8, 2007
 * Time: 11:46:27 AM
 */
public class Sequence {

    protected String s;

    final String bases = "TCAG";
    final String codons = "ACDEFGHIKLMNPQRSTVWYZ";    
    final String standardGeneticCode = "FFLLSSSSYYZZCCZWLLLLPPPPHHQQRRRRIIIMTTTTNNKKSSRRVVVVAAAADDEEGGGG";

    public Sequence(){

    }

    public Sequence(String s){
        this.s = s;
    }

    public boolean isProteinSequence(){
        int i;
        for (i = 0; i < codons.length(); i++){
            if (s.indexOf(codons.charAt(i)) >= 0 && bases.indexOf(codons.charAt(i)) == -1){
                return true;
            }
        }
        return false;
    }

    public void readSequenceEmbl(String filename){
        Scanner sc;
        String tmp;
        Pattern p = Pattern.compile("[a-z&&[^jo]]+");
        try{
            sc = new Scanner(new File(filename));
            tmp = sc.nextLine();
            while (!tmp.startsWith("SQ")){
                tmp = sc.nextLine();
            }
            s = "";
            tmp = sc.next();
            while (!tmp.startsWith("//")){
                if (tmp.matches(p.toString())){
                    s += tmp.toUpperCase();
                }
                tmp = sc.next();
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
    }

    public void readSequenceGenBank(String filename){
        Scanner sc;
        String tmp;
        Pattern p = Pattern.compile("[a-z&&[^jo]]+");
        try{
            sc = new Scanner(new File(filename));
            tmp = sc.nextLine();
            while (!tmp.startsWith("ORIGIN")){
                tmp = sc.nextLine();
            }
            s = "";
            tmp = sc.next();
            while (!tmp.startsWith("//")){
                if (tmp.matches(p.toString())){
                    s += tmp.toUpperCase();
                }
                tmp = sc.next();
            }
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
    }

    public void readSequenceFASTA(String filename){
        Scanner sc;
        String tmp;
        try{
            sc = new Scanner(new File(filename));
            sc.nextLine();
            s = "";
            do{
                tmp = sc.nextLine();
                s += tmp.toUpperCase();
            }while (sc.hasNextLine());
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println("File " + filename + " not found");
        }
    }

    public Sequence shuffle(){
        int i, j;
        char[] s;
        char tmp;
        s = get().toCharArray();
        for (i = 0; i < s.length - 1; i++){
            j = (int) (Math.random() * (s.length - i));
            tmp = s[i];
            s[i] = s[j];
            s[j] = tmp;
        }
        return new Sequence(s.toString());
    }

    public Sequence extend(int start, int end){
        int i;
        String result = this.s;
        for (i = 0; i < start; i++){
            result = '-' + result;
        }
        for (i = 0; i < end; i++){
            result = result + '-';
        }
        return new Sequence(result);
    }

    public void extendItself(int start, int end){
        int i;
        String result = this.s;
        for (i = 0; i < start; i++){
            result = '-' + result;
        }
        for (i = 0; i < end; i++){
            result = result + '-';
        }
        this.s = result;
    }

    public int codeCount(int start, int end){
        int i, count = 0;
        for (i = start + 1; i < end; i++){
            if (charAt(i) != '-'){
                count++;
            }
        }
        return count;
    }

    public void insertGap(int start, int length){
        int i;
        String result = "";
        if (start != 0){
            result += this.s.substring(0, start);
        }
        for (i = 0; i < length; i++){
            result += '-';
        }
        if (start < this.s.length()){
            result += this.s.substring(start, this.s.length());
        }
        this.s = result;
    }

    public int length(){
        return s.length();
    }

    public int samePosition(int pos, Sequence seq){
        int i, codeCount = 0, returnPos;
        for (i = 0; i < pos; i++){
            if (charAt(i) != '-'){
                codeCount++;
            }
        }
        returnPos = 0;
        while (codeCount > 0){
            if (seq.charAt(returnPos) != '-'){
                codeCount--;
            }
            returnPos++;
        }
        return returnPos;
    }

    public int overlap(Sequence seq){
        String t;
        int i, j, start;
        boolean overlapped;
        t = seq.get();
        if (s.length() < t.length()){
            start = 0;
        }
        else{
            start = s.length() - t.length();
        }
        for (i = start; i < s.length(); i++){
            if (s.charAt(i) == t.charAt(0)){
                overlapped = true;
                for (j = i + 1; j < s.length(); j++){
                    if (s.charAt(j) != t.charAt(j - i)){
                        overlapped = false;
                        break;
                    }
                }
                if (overlapped){
                    return s.length() - i;
                }
            }
        }
        return 0;
    }

    public void cat(Sequence seq){
        int overlapAmount, length;
        overlapAmount = overlap(seq);
        length = seq.length();
        if (overlapAmount != length){
            s += seq.get().substring(overlapAmount, length);
        }
    }

    public char charAt(int index){
        return s.charAt(index);
    }

    public String get(){
        return s;
    }

    public void set(String s){
        this.s = s;
    }

}
