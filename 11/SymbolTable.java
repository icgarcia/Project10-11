import java.util.*;

public class SymbolTable 
{
    private HashMap<String,Symbol> classSymbols;
    private HashMap<String,Symbol> subroutineSymbols;
    private HashMap<Symbol.KIND,Integer> indices;

    public SymbolTable() 
	{
        classSymbols = new HashMap<String, Symbol>();
        subroutineSymbols = new HashMap<String, Symbol>();

        indices = new HashMap<Symbol.KIND, Integer>();
        indices.put(Symbol.KIND.ARG,0);
    }

    public void startSubroutine()
	{
        subroutineSymbols.clear();
        indices.put(Symbol.KIND.ARG,0);
    }

    public void define(String name, String type, Symbol.KIND kind)
	{
        if (kind == Symbol.KIND.ARG)
		{
            int index = indices.get(kind);
            Symbol symbol = new Symbol(type,kind,index);
            indices.put(kind,index+1);
            subroutineSymbols.put(name,symbol);
        }
    }

    public Symbol.KIND kindOf(String name)
	{
        Symbol symbol = lookUp(name);

        if (symbol != null) 
			return symbol.getKind();

        return Symbol.KIND.NONE;
    }

    public String typeOf(String name)
	{
        Symbol symbol = lookUp(name);

        if (symbol != null) 
			return symbol.getType();

        return "";
    }

    public int indexOf(String name)
	{
        Symbol symbol = lookUp(name);

        if (symbol != null) 
			return symbol.getIndex();

        return -1;
    }
}
