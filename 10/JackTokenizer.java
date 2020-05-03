import java.io.*;
import java.util.*;

public class JackTokenizer 
{
    private Scanner scanner;
    private String token;
    private int tokenType;
    private int pointer;
    private ArrayList<String> tokenArray;
	
    public final static int SYMBOL = 1;
    public final static int INT_CONST = 2;
    public final static int STRING_CONST = 3;
    public final static int CLASS = 4;
    public final static int METHOD = 5;
    public final static int FUNCTION = 6;
    public final static int INT = 7;
	public final static int CHAR = 8;
    public final static int BOOLEAN = 9;
	public final static int IF = 10;
    public final static int ELSE = 11;
	public final static int DO = 12;
	public final static int WHILE = 13;
    public final static int LET = 14;
    public final static int TRUE = 15;
    public final static int FALSE = 16;
	public final static int RETURN = 17;

    private static Pattern tokenPatterns;
    private static String keyWordReg;
    private static String intReg;
    private static String strReg;
	private static String symbolReg;
    private static String idReg;

    private static HashMap<String,Integer> keyWordMap = new HashMap<String, Integer>();
    private static HashSet<Character> opSet = new HashSet<Character>();

    public static
	{
        keyWordMap.put("class",CLASS);keyWordMap.put("function",FUNCTION);
        keyWordMap.put("method",METHOD);keyWordMap.put("int",INT);
		keyWordMap.put("char",CHAR);keyWordMap.put("boolean",BOOLEAN);
		keyWordMap.put("true",TRUE);keyWordMap.put("false",FALSE);
        keyWordMap.put("let",LET);keyWordMap.put("do",DO);keyWordMap.put("if",IF);
        keyWordMap.put("else",ELSE);keyWordMap.put("while",WHILE);keyWordMap.put("return",RETURN);

        opSet.add('+');opSet.add('-');opSet.add('*');opSet.add('/');opSet.add('&');opSet.add('|');
        opSet.add('<');opSet.add('>');opSet.add('=');
    }

    public JackTokenizer(File fileIn) 
	{
        try 
		{
            scanner = new Scanner(fileIn);
            String preprocessed = "";
            String line = "";

            while(scanner.hasNext())
			{
                line = scanner.nextLine().trim();
                if (line.length() > 0) 
                    preprocessed += line + "\n";
            }
            preprocessed = preprocessed.trim();

            initRegs();

            Matcher m = tokenPatterns.matcher(preprocessed);
            tokenArray = new ArrayList<String>();
            pointer = 0;

            while (m.find())
                tokenArray.add(m.group());

        } 
		catch (FileNotFoundException e) 
            e.printStackTrace();

        token = "";
        tokenType = -1;
    }

    private void initRegs()
	{
        keyWordReg = "";

        for (String seg: keyWordMap.keySet())
            keyWordReg += seg + "|";

        symbolReg = "[\\&\\*\\+\\(\\)\\.\\/\\,\\-\\]\\;\\~\\}\\|\\{\\>\\=\\[\\<]";
        intReg = "[0-9]+";
        strReg = "\"[^\"\n]*\"";
        idReg = "[\\w_]+";

        tokenPatterns = Pattern.compile(keyWordReg + symbolReg + "|" + intReg + "|" + strReg + "|" + idReg);
    }

    public boolean hasMoreTokens() 
	{
        return pointer < tokenArray.size();
    }

    public void advance()
	{
        if (hasMoreTokens()) 
		{
            token = tokenArray.get(pointer);
            pointer++;
        }
		else
            throw new IllegalStateException("No more tokens");
        if (token.matches(keyWordReg))
            tokenType = KEYWORD;
        else if (token.matches(symbolReg))
            type = SYMBOL;
        else if (token.matches(intReg))
            tokenType = INT_CONST;
        else if (token.matches(strReg))
            tokenType = STRING_CONST;
        else
            throw new IllegalArgumentException("Unknown token:" + token);
    }

    public String getCurrentToken() 
	{
        return token;
    }

    public int tokenType()
	{
        return tTokenType;
    }

    public char symbol()
	{
        if (tokenType == SYMBOL)
            return token.charAt(0);
        else
            throw new IllegalStateException("Current token is not a symbol!");
    }

    public int intVal()
	{
        if(tokenType == INT_CONST)
            return Integer.parseInt(token);
        else
            throw new IllegalStateException("Current token is not an integer constant!");
    }

    public String stringVal()
	{
        if (tokenType == STRING_CONST)
            return token.substring(1, token.length() - 1);
        else
            throw new IllegalStateException("Current token is not a string constant!");
    }

    public boolean isOp()
	{
        return opSet.contains(symbol());
    }

    public static String noSpaces(String strIn)
	{
        String result = "";

        if (strIn.length() != 0)
		{
            String[] segs = strIn.split(" ");

            for (String s: segs)
                result += s;
        }
        return result;
    }
}