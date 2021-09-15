package ltd.fdsa.switcher.core.model;

/*
 * 自描述数据结构
 * 第一个byte作为类型，根据类型不同取N个bytes作为内容的长度length。再取length个类型的单元作为内容
 * 注：定长类型可以直取内容，以下为类型的编号
 * */
public interface Item {

    Item parse(byte[] bytes);

    byte[] toBytes();

    Object getValue();

    Type getType();

    enum Type {
        Bytes,// N bytes
        Byte0,// 定长直取1 byte
        Byte1,// 2^8 bytes
        Byte2,// 2^16 bytes
        Byte3,// 2^32 bytes
        Byte4,// 2^64 bytes
        Byte5,// 2^0 bytes
        Byte6,// 2^0 bytes
        Byte7,// 2^0 bytes
        Byte8,// 2^8 bytes
        Byte16,// 2^16 bytes
        Byte32,// 2^32 bytes
        Byte64,// 2^64 bytes

        BOOLs,// N BOOLs
        BOOL0,// 定长直取1 byte
        BOOL1,// 2^8 BOOLs
        BOOL2,// 2^16 BOOLs
        BOOL3,// 2^32 BOOLs
        BOOL4,// 2^64 BOOLs
        BOOL5,// 2^0 BOOLs
        BOOL6,// 2^0 BOOLs
        BOOL7,// 2^0 BOOLs
        BOOL8,// 2^8 BOOLs
        BOOL16,// 2^16 BOOLs
        BOOL32,// 2^32 BOOLs
        BOOL64,// 2^64 BOOLs


        Ones,// N Ones
        One0,// 定长直取1 byte
        One1,// 2^8 Ones
        One2,// 2^16 Ones
        One3,// 2^32 Ones
        One4,// 2^64 Ones
        One5,// 2^0 Ones
        One6,// 2^0 Ones
        One7,// 2^0 Ones
        One8,// 2^8 Ones
        One16,// 2^16 Ones
        One32,// 2^32 Ones
        One64,// 2^64 Ones

        Chars,// N Chars
        Char0,// 定长直取1 Char
        Char1,// 2^8 Chars
        Char2,// 2^16 Chars
        Char3,// 2^32 Chars
        Char4,// 2^64 Chars
        Char5,// 2^0 Chars
        Char6,// 2^0 Chars
        Char7,// 2^0 Chars
        Char8,// 2^8 Chars
        Char16,// 2^16 Chars
        Char32,// 2^32 Chars
        Char64,// 2^64 Chars

        Shorts,// N Shorts
        Short0,// 定长直取1 Short
        Short1,// 2^8 Shorts
        Short2,// 2^16 Shorts
        Short3,// 2^32 Shorts
        Short4,// 2^64 Shorts
        Short5,// 2^0 Shorts
        Short6,// 2^0 Shorts
        Short7,// 2^0 Shorts
        Short8,// 2^8 Shorts
        Short16,// 2^16 Shorts
        Short32,// 2^32 Shorts
        Short64,// 2^64 Shorts

        Twos,// N Bytes
        Two0,// 定长直取2 byte
        Two1,// 2^8 Twos
        Two2,// 2^16 Twos
        Two3,// 2^32 Twos
        Two4,// 2^64 Twos
        Two5,// 2^0 Twos
        Two6,// 2^0 Twos
        Two7,// 2^0 Twos
        Two8,// 2^8 Twos
        Two16,// 2^16 Twos
        Two32,// 2^32 Twos
        Two64,// 2^64 Twos


        INTs,// N INTs
        INT0,// 定长直取2^0 INTs
        INT1,// 2^8 INTs
        INT2,// 2^16 INTs
        INT3,// 2^32 INTs
        INT4,// 2^64 INTs
        INT5,// 2^0 INTs
        INT6,// 2^0 INTs
        INT7,// 2^0 INTs
        INT8,// 2^8 INTs
        INT16,// 2^16 INTs
        INT32,// 2^32 INTs
        INT64,// 2^64 INTs

        Floats,// N Floats
        Float0,// 定长直取2^0 Floats
        Float1,// 2^8 Floats
        Float2,// 2^16 Floats
        Float3,// 2^32 Floats
        Float4,// 2^64 Floats
        Float5,// 2^0 Floats
        Float6,// 2^0 Floats
        Float7,// 2^0 Floats
        Float8,// 2^8 Floats
        Float16,// 2^16 Floats
        Float32,// 2^32 Floats
        Float64,// 2^64 Floats

        Threes,// N Threes
        Three0,// 定长直取2^0 Threes
        Three1,// 2^8 Threes
        Three2,// 2^16 Threes
        Three3,// 2^32 Threes
        Three4,// 2^64 Threes
        Three5,// 2^0 Threes
        Three6,// 2^0 Threes
        Three7,// 2^0 Threes
        Three8,// 2^8 Threes
        Three16,// 2^16 Threes
        Three32,// 2^32 Threes
        Three64,// 2^64 Threes


        Fours,// N Fours
        Four0,// 定长直取2^0 Fours
        Four1,// 2^8 Fours
        Four2,// 2^16 Fours
        Four3,// 2^32 Fours
        Four4,// 2^64 Fours
        Four5,// 2^0 Fours
        Four6,// 2^0 Fours
        Four7,// 2^0 Fours
        Four8,// 2^8 Fours
        Four16,// 2^16 Fours
        Four32,// 2^32 Fours
        Four64,// 2^64 Fours

        LONGs,// N LONGs
        LONG0,// 定长直取2^0 LONGs
        LONG1,// 2^8 LONGs
        LONG2,// 2^16 LONGs
        LONG3,// 2^32 LONGs
        LONG4,// 2^64 LONGs
        LONG5,// 2^0 LONGs
        LONG6,// 2^0 LONGs
        LONG7,// 2^0 LONGs
        LONG8,// 2^8 LONGs
        LONG16,// 2^16 LONGs
        LONG32,// 2^32 LONGs
        LONG64,// 2^64 LONGs

        Doubles,// N Doubles
        Double0,// 定长直取2^0 Doubles
        Double1,// 2^8 Doubles
        Double2,// 2^16 Doubles
        Double3,// 2^32 Doubles
        Double4,// 2^64 Doubles
        Double5,// 2^0 Doubles
        Double6,// 2^0 Doubles
        Double7,// 2^0 Doubles
        Double8,// 2^8 Doubles
        Double16,// 2^16 Doubles
        Double32,// 2^32 Doubles
        Double64,// 2^64 Doubles


        DATEs,// N DATEs
        DATE0,// 定长直取2^0 DATEs
        DATE1,// 2^8 DATEs
        DATE2,// 2^16 DATEs
        DATE3,// 2^32 DATEs
        DATE4,// 2^64 DATEs
        DATE5,// 2^0 DATEs
        DATE6,// 2^0 DATEs
        DATE7,// 2^0 DATEs
        DATE8,// 2^8 DATEs
        DATE16,// 2^16 DATEs
        DATE32,// 2^32 DATEs
        DATE64,// 2^64 DATEs

        Fives,// N Fives
        Five0,// 定长直取2^0 Fives
        Five1,// 2^8 Fives
        Five2,// 2^16 Fives
        Five3,// 2^32 Fives
        Five4,// 2^64 Fives
        Five5,// 2^0 Fives
        Five6,// 2^0 Fives
        Five7,// 2^0 Fives
        Five8,// 2^8 Fives
        Five16,// 2^16 Fives
        Five32,// 2^32 Fives
        Five64,// 2^64 Fives

        Sixes,// N Sixes
        Six0,// 定长直取2^0 Sixes
        Six1,// 2^8 Sixes
        Six2,// 2^16 Sixes
        Six3,// 2^32 Sixes
        Six4,// 2^64 Sixes
        Six5,// 2^0 Sixes
        Six6,// 2^0 Sixes
        Six7,// 2^0 Sixes
        Six8,// 2^8 Sixes
        Six16,// 2^16 Sixes
        Six32,// 2^32 Sixes
        Six64,// 2^64 Sixes

        Sevens,// N Sevens
        Seven0,// 定长直取2^0 Sevens
        Seven1,// 2^8 Sevens
        Seven2,// 2^16 Sevens
        Seven3,// 2^32 Sevens
        Seven4,// 2^64 Sevens
        Seven5,// 2^0 Sevens
        Seven6,// 2^0 Sevens
        Seven7,// 2^0 Sevens
        Seven8,// 2^8 Sevens
        Seven16,// 2^16 Sevens
        Seven32,// 2^32 Sevens
        Seven64,// 2^64 Sevens


        Eights,// N Eights
        Eight0,// 定长直取2^0 Eights
        Eight1,// 2^8 Eights
        Eight2,// 2^16 Eights
        Eight3,// 2^32 Eights
        Eight4,// 2^64 Eights
        Eight5,// 2^0 Eights
        Eight6,// 2^0 Eights
        Eight7,// 2^0 Eights
        Eight8,// 2^8 Eights
        Eight16,// 2^16 Eights
        Eight32,// 2^32 Eights
        Eight64,// 2^64 Eights
    }
}
