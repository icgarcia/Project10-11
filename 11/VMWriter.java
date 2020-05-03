import java.io.*;
import java.util.*;

public class VMWriter 
{
    public static enum SEGMENT {CONST,ARG,THIS,THAT};
    public static enum COMMAND {ADD,SUB,NEG,EQ,LT,AND,OR,NOT};

    private static HashMap<SEGMENT,String> segmentMap = new HashMap<SEGMENT, String>();
    private static HashMap<COMMAND,String> commandMap = new HashMap<COMMAND, String>();
    private PrintWriter pW;

    public static 
	{
        segmentMap.put(SEGMENT.CONST,"constant");
        segmentMap.put(SEGMENT.ARG,"argument");
        segmentMap.put(SEGMENT.THIS,"this");
        segmentMap.put(SEGMENT.THAT,"that");

        commandMap.put(COMMAND.ADD,"add");
        commandMap.put(COMMAND.SUB,"sub");
        commandMap.put(COMMAND.NEG,"neg");
        commandMap.put(COMMAND.EQ,"eq");
        commandMap.put(COMMAND.LT,"lt");
        commandMap.put(COMMAND.AND,"and");
        commandMap.put(COMMAND.OR,"or");
        commandMap.put(COMMAND.NOT,"not");
    }

    public VMWriter(File fOut) 
	{

        try 
            pW = new PrintWriter(fOut);
		catch (FileNotFoundException e) 
            e.printStackTrace();
    }

    public void writePush(SEGMENT segment, int index)
	{
        writeCommand("push",segmentMap.get(segment),String.valueOf(index));
    }

    public void writePop(SEGMENT segment, int index)
	{
        writeCommand("pop",segmentMap.get(segment),String.valueOf(index));
    }

    public void writeArithmetic(COMMAND command)
	{
        writeCommand(commandMap.get(command),"","");
    }

    public void writeGoto(String label)
	{
        writeCommand("goto",label,"");
    }

    public void writeReturn()
	{
        writeCommand("return","","");
    }

    public void writeCommand(String cmd, String arg1, String arg2)
	{

        pW.print(cmd + " " + arg1 + " " + arg2 + "\n");

    }

    public void close()
	{
        pW.close();
    }
}
