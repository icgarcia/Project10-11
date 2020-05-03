import java.io.*;

public class CompilationEngine 
{
    private PrintWriter pW;
    private PrintWriter tokenPW;
    private JackTokenizer tokenizer;

    public CompilationEngine(File fileIn, File fileOut, File outTokenFile) 
	{
        try 
		{
            tokenizer = new JackTokenizer(fileIn);
            pW = new PrintWriter(fileOut);
            tokenPW = new pW(outTokenFile);

        } 
		catch (FileNotFoundException e) 
            e.printStackTrace();
    }

	public void compileClass()
	{
        tokenizer.advance();

        if (tokenizer.tokenType() != JackTokenizer.KEYWORD || tokenizer.keyWord() != JackTokenizer.CLASS)
            error("class");

        pW.print("<class>\n");
        tokenPW.print("<tokens>\n");

        pW.print("<keyword>class</keyword>\n");
        tokenPW.print("<keyword>class</keyword>\n");

        tokenizer.advance();

        if (tokenizer.tokenType() != JackTokenizer.IDENTIFIER)
            error("className");

        pW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
        tokenPW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

        compileSubroutine();

        if (tokenizer.hasMoreTokens())
            throw new IllegalStateException("Unexpected tokens");

        tokenPW.print("</tokens>\n");
        pW.print("</class>\n");

        pW.close();
        tokenPW.close();
    }

    private void compileType()
	{
        tokenizer.advance();
        boolean isType = false;

        if (tokenizer.tokenType() == JackTokenizer.KEYWORD && (tokenizer.keyWord() == JackTokenizer.INT || tokenizer.keyWord() == JackTokenizer.CHAR || tokenizer.keyWord() == JackTokenizer.BOOLEAN))
		{
            pW.print("<keyword>" + tokenizer.getCurrentToken() + "</keyword>\n");
            tokenPW.print("<keyword>" + tokenizer.getCurrentToken() + "</keyword>\n");
            isType = true;
        }

        if (tokenizer.tokenType() == JackTokenizer.IDENTIFIER)
		{
            pW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
            tokenPW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
            isType = true;
        }

        if (!isType) 
			error("in|char|boolean|className");
    }

    private void compileSubroutine()
	{
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '}')
		{
            tokenizer.pointerBack();
            return;
        }

        if (tokenizer.tokenType() != JackTokenizer.KEYWORD || (tokenizer.keyWord() != JackTokenizer.CONSTRUCTOR && tokenizer.keyWord() != JackTokenizer.FUNCTION && tokenizer.keyWord() != JackTokenizer.METHOD))
            error("constructor|function|method");

        pW.print("<subroutineDec>\n");

        pW.print("<keyword>" + tokenizer.getCurrentToken() + "</keyword>\n");
        tokenPW.print("<keyword>" + tokenizer.getCurrentToken() + "</keyword>\n");

        tokenizer.advance();
        if (tokenizer.tokenType() == JackTokenizer.KEYWORD && tokenizer.keyWord() == JackTokenizer.VOID)
		{
            pW.print("<keyword>void</keyword>\n");
            tokenPW.print("<keyword>void</keyword>\n");
        }
		else 
		{
            tokenizer.pointerBack();
            compileType();
        }

        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.IDENTIFIER)
            error("subroutineName");

        pW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
        tokenPW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

        pW.print("<parameterList>\n");
        compileParameterList();
        pW.print("</parameterList>\n");

        pW.print("</subroutineDec>\n");

        compileSubroutine();
    }

    private void compileStatement()
	{
        tokenizer.advance();

        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '}')
		{
            tokenizer.pointerBack();
            return;
        }

        if (tokenizer.tokenType() != JackTokenizer.KEYWORD)
            error("keyword");
		else 
		{
            switch (tokenizer.keyWord())
			{
                case JackTokenizer.LET:compileLet();break;
                case JackTokenizer.IF:compileIf();break;
                case JackTokenizer.WHILE:compilesWhile();break;
                case JackTokenizer.DO:compileDo();break;
                case JackTokenizer.RETURN:compileReturn();break;
                default:error("'let'|'if'|'while'|'do'|'return'");
            }
        }
        compileStatement();
    }

    private void compileParameterList()
	{
        tokenizer.advance();
        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == ')')
		{
            tokenizer.pointerBack();
            return;
        }

        tokenizer.pointerBack();
        do 
		{
            compileType();

            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.IDENTIFIER)
                error("identifier");
            pW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
            tokenPW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

            tokenizer.advance();
            if (tokenizer.tokenType() != JackTokenizer.SYMBOL || (tokenizer.symbol() != ',' && tokenizer.symbol() != ')'))
                error("',' or ')'");

            if (tokenizer.symbol() == ',')
			{
                pW.print("<symbol>,</symbol>\n");
                tokenPW.print("<symbol>,</symbol>\n");
            }
			else 
			{
                tokenizer.pointerBack();
                break;
            }

        }
		while(true);
    }
	
	private void compileIf()
	{
        pW.print("<ifStatement>\n");

        pW.print("<keyword>if</keyword>\n");
        tokenPW.print("<keyword>if</keyword>\n");
        pW.print("<statements>\n");
        compileStatement();
        pW.print("</statements>\n");

        tokenizer.advance();
        if (tokenizer.tokenType() == JackTokenizer.KEYWORD && tokenizer.keyWord() == JackTokenizer.ELSE)
		{
            pW.print("<keyword>else</keyword>\n");
            v.print("<keyword>else</keyword>\n");
            pW.print("<statements>\n");
            compileStatement();
            pW.print("</statements>\n");
        }
		else
            tokenizer.pointerBack();
        pW.print("</ifStatement>\n");

    }

    private void compileDo()
	{
        pW.print("<doStatement>\n");

        pW.print("<keyword>do</keyword>\n");
        tokenPW.print("<keyword>do</keyword>\n");
        pW.print("</doStatement>\n");
    }
	
	private void compilesWhile()
	{
        pW.print("<whileStatement>\n");

        ppW.print("<keyword>while</keyword>\n");
        tokenPW.print("<keyword>while</keyword>\n");
        pW.print("<statements>\n");
        compileStatement();
        pW.print("</statements>\n");
        pW.print("</whileStatement>\n");
    }

    private void compileLet()
	{
        pW.print("<letStatement>\n");

        pW.print("<keyword>let</keyword>\n");
        tokenPW.print("<keyword>let</keyword>\n");

        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.IDENTIFIER)
            error("varName");

        pW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");
        tokenPW.print("<identifier>" + tokenizer.identifier() + "</identifier>\n");

        tokenizer.advance();
        if (tokenizer.tokenType() != JackTokenizer.SYMBOL || (tokenizer.symbol() != '[' && tokenizer.symbol() != '='))
            error("'['|'='");

        boolean expExist = false;

        if (tokenizer.symbol() == '[')
		{
            expExist = true;

            pW.print("<symbol>[</symbol>\n");
            tokenPW.print("<symbol>[</symbol>\n");

            tokenizer.advance();
            if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == ']')
			{
                pW.print("<symbol>]</symbol>\n");
                tokenPW.print("<symbol>]</symbol>\n");
            }
			else
                error("']'");
        }

        if (expExist) tokenizer.advance();

        pW.print("<symbol>=</symbol>\n");
        tokenPW.print("<symbol>=</symbol>\n");
        pW.print("</letStatement>\n");
    }
	
	private void compileReturn()
	{
        pW.print("<returnStatement>\n");

        pW.print("<keyword>return</keyword>\n");
        tokenPW.print("<keyword>return</keyword>\n");
        tokenizer.advance();
        if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == ';')
		{
            pW.print("<symbol>;</symbol>\n");
            tokenPW.print("<symbol>;</symbol>\n");
            pW.print("</returnStatement>\n");
            return;
        }
        tokenizer.pointerBack();
        pW.print("</returnStatement>\n");
    }

    private void compileTerm()
	{
        pW.print("<term>\n");

        tokenizer.advance();
        if (tokenizer.tokenType() == JackTokenizer.IDENTIFIER)
		{
            String tempId = tokenizer.identifier();

            tokenizer.advance();
            if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '[')
			{
                pW.print("<identifier>" + tempId + "</identifier>\n");
                tokenPW.print("<identifier>" + tempId + "</identifier>\n");
                pW.print("<symbol>[</symbol>\n");
                tokenPW.print("<symbol>[</symbol>\n");
            }
			else if (tokenizer.tokenType() == JackTokenizer.SYMBOL && (tokenizer.symbol() == '(' || tokenizer.symbol() == '.')){
                tokenizer.pointerBack();tokenizer.pointerBack();
                compileSubroutineCall();
            }
			else 
			{
                pW.print("<identifier>" + tempId + "</identifier>\n");
                tokenPW.print("<identifier>" + tempId + "</identifier>\n");
                tokenizer.pointerBack();
            }
        }
		else
		{
            if (tokenizer.tokenType() == JackTokenizer.INT_CONST)
			{
                pW.print("<integerConstant>" + tokenizer.intVal() + "</integerConstant>\n");
                tokenPW.print("<integerConstant>" + tokenizer.intVal() + "</integerConstant>\n");
            }
			else if (tokenizer.tokenType() == JackTokenizer.STRING_CONST)
			{
                pW.print("<stringConstant>" + tokenizer.stringVal() + "</stringConstant>\n");
                tokenPW.print("<stringConstant>" + tokenizer.stringVal() + "</stringConstant>\n");
            }
			else if(tokenizer.tokenType() == JackTokenizer.KEYWORD &&
                            (tokenizer.keyWord() == JackTokenizer.TRUE ||
                            tokenizer.keyWord() == JackTokenizer.FALSE ||
                            tokenizer.keyWord() == JackTokenizer.NULL ||
                            tokenizer.keyWord() == JackTokenizer.THIS))
			{
                    pW.print("<keyword>" + tokenizer.getCurrentToken() + "</keyword>\n");
                    tokenPW.print("<keyword>" + tokenizer.getCurrentToken() + "</keyword>\n");
            }
			else if (tokenizer.tokenType() == JackTokenizer.SYMBOL && tokenizer.symbol() == '(')
			{
                pW.print("<symbol>(</symbol>\n");
                tokenPW.print("<symbol>(</symbol>\n");
            }
			else if (tokenizer.tokenType() == JackTokenizer.SYMBOL && (tokenizer.symbol() == '-' || tokenizer.symbol() == '~')){
                pW.print("<symbol>" + tokenizer.symbol() + "</symbol>\n");
                tokenPW.print("<symbol>" + tokenizer.symbol() + "</symbol>\n");
                compileTerm();
            }
			else
                error("integerConstant|stringConstant|keywordConstant|'(' expression ')'|unaryOp term");
        }
        pW.print("</term>\n");
    }

    private void error(String val)
	{
        throw new IllegalStateException("Expected token missing : " + val + " Current token:" + tokenizer.getCurrentToken());
    }
}