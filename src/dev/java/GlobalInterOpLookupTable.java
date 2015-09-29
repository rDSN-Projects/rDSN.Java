package dsn.dev.java;

import java.util.ArrayList;

public class GlobalInterOpLookupTable {
	public static int Put(Object obj)
    {
        int table_id = (int) Thread.currentThread().getId() % _table_count;
        int idx = _tables[table_id].Put(obj);
        Logging.dassert(idx <= 0x07ffffff, "too many concurrent objects in global lookup table now");
        return (table_id << 27) + idx;
    }

    public static Object Get(int index)
    {
        int table_id = index >> 27;
        int idx = index & 0x07ffffff;
        return _tables[table_id].Get(idx);
    }

    public static Object GetRelease(int index)
    {
        int table_id = index >> 27;
        int idx = index & 0x07ffffff;
        return _tables[table_id].GetRelease(idx);
    }

    private static InterOpLookupTable[] _tables = InitTables(100, 997);
    private static int _table_count;

    private static InterOpLookupTable[] InitTables(int init_slot_count_per_table, int table_count)
    {
        _table_count = table_count;
        ArrayList<InterOpLookupTable> tables = new ArrayList<InterOpLookupTable>();
        for (int i = 0; i < table_count; i++)
        {
        	InterOpLookupTable table = new InterOpLookupTable(init_slot_count_per_table);
            tables.add(table);
        }
        return (InterOpLookupTable[]) tables.toArray(new InterOpLookupTable[0]);
    }
    
}
