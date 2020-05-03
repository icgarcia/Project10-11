import java.io.*;
import java.util.*;

public class JackCompiler 
{
    public static ArrayList<File> getJackFiles(File dir)
	{
        File[] files = dir.listFiles();
        ArrayList<File> result = new ArrayList<File>();

        if (files == null) 
			return result;

        for (File f:files)
		{
            if (f.getName().endsWith(".jack"))
                result.add(f);
        }
        return result;
    }

    public static void main(String[] args) 
	{
            String fileIn = args[0];
            File file = new File(fileIn);
            String fileOutPath = "";
			String tokenFileOutPath = "";
            File fileOut;
			File tokenFileOut;
            ArrayList<File> jackFiles = new ArrayList<File>();

            if (file.isFile()) 
			{
                jackFiles.add(file);
            }
			else if (file.isDirectory()) 
			{
                jackFiles = getJackFiles(file);
                if (jackFiles.size() == 0)
                    throw new IllegalArgumentException("No jack files found!");
            }

            for (File f: jackFiles) 
			{
                fileOutPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".")) + ".xml";
                tokenFileOutPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(".")) + "T.xml";
                fileOut = new File(fileOutPath);
                tokenFileOut = new File(tokenFileOutPath);

                CompilationEngine compilationEngine = new CompilationEngine(f,fileOut,tokenFileOut);
                compilationEngine.compileClass();

                System.out.println("File created : " + fileOutPath);
                System.out.println("File created : " + tokenFileOutPath);
            }
    }
}