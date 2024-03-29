grammar Fql;cn.zhumingwu
import Common;

@header{package cn.zhumingwu.database.fql.antlr;}

@lexer::members {
    public boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }
    public boolean isNameStart(int c) {
        return '_' == c ||
          (c >= 'A' && c <= 'Z') ||
          (c >= 'a' && c <= 'z');
    }
    public boolean isDot(int c) {
        return '.' == c;
    }
}


document : selectionSet;

selectionSet :  '{' selection+ '}';

selection :
// ? 表示可有可无
// + 表示至少有一个
// | 表示或的关系
// * 表示有0或者多个
alias? name arguments* selectionSet?;
arguments : '(' argument+ ')';
alias : name ':';


