/*
假设你想提供一些基本的对象/关系映射功能，能够自动生成数据库表。
你可以使用 XML 描述文件来指明类的名字、每个成员以及数据库映射的相关信息。

但是，通过使用注解，你可以把所有信息都保存在 JavaBean 源文件中。
为此你需要一些用于定义数据库表名称、数据库列以及将 SQL 类型映射到属性的注解。

一下是自定义的几个注解：
@DBTable、@Constraints、@SQLString、@SQLInteger
 */

import atunit.database.Constraints;
import atunit.database.DBTable;
import atunit.database.SQLInteger;
import atunit.database.SQLString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 下面是一个简单的，使用了如上注解的类：
 */
@DBTable(name = "member")
class Member {

    @SQLString(30)
    String firstName;

    @SQLString(50)
    String lastName;

    @SQLInteger
    Integer age;

    @SQLString(value = 30, constraints = @Constraints(primaryKey = true))
    String reference;

    static int memberCount;

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Integer getAge() {
        return age;
    }
    public String getReference() {
        return reference;
    }
    @Override
    public String toString() {
        return reference;
    }
}

/*
默认值的语法虽然很灵巧，但是它很快就变的复杂起来。
以 reference 字段的注解为例，上面拥有 @SQLString 注解，
但是这个字段也将成为表的主键，因此在嵌入的 @Constraint 注解中设定 primaryKey 元素的值。

这时事情就变的复杂了。
你不得不为这个嵌入的注解使用很长的键—值对的形式，来指定元素名称和 @interface 的名称。
同时，由于有特殊命名的 value 也不是唯一需要赋值的元素，因此不能再使用快捷方式特性。
如你所见，最终结果不算清晰易懂（注解里面要些嵌套一长串去说明）。
 */


/**
 * 【实现处理器】
 */
class TableCreator {
    public static void main(String[] args) throws ClassNotFoundException {
        args = new String[] {"Member"};
        if (args.length < 1) {
            System.out.println("arguments: annotated classes");
            System.exit(0);
        }

        for (String className : args) {
            Class<?> cl = Class.forName(className);
            DBTable dbTable = cl.getAnnotation(DBTable.class);
            if (dbTable == null) {
                System.out.println("No DBTable annotations in class " + className);
                continue;
            }
            String tableName = dbTable.name();
            // If the name is empty, use the Class name:
            if (tableName.length() < 1) {
                tableName = cl.getName().toUpperCase();
            }
            List<String> columnDefs = new ArrayList<>();
            for (Field fied : cl.getDeclaredFields()) {
                String columnName;
                for (Annotation ann : fied.getDeclaredAnnotations()) {
                    if (ann instanceof SQLInteger) {
                        SQLInteger sInt = (SQLInteger) ann;
                        // Use field name if name not specified.
                        if (sInt.name().length() < 1) {
                            columnName = fied.getName().toUpperCase();
                        } else {
                            columnName = sInt.name();
                        }
                        columnDefs.add(columnName + " INT" + getConstraints(sInt.constraints()));
                    }
                    if (ann instanceof SQLString) {
                        SQLString sString = (SQLString) ann;
                        // Use field name if name not specified.
                        if (sString.name().length() < 1) {
                            columnName = fied.getName().toUpperCase();
                        } else {
                            columnName = sString.name();
                        }
                        columnDefs.add(columnName +
                                " VARCHAR(" + sString.value() + ")" +
                                getConstraints(sString.constraints()));
                    }
                }
            }
            StringBuilder createCommand = new StringBuilder("CREATE TABLE " + tableName + "(");
            for (String columnDef : columnDefs) {
                createCommand.append("\n").append(columnDef).append(",");
            }
            // Remove trailing comma
            String tableCreate = createCommand.substring(0, createCommand.length() - 1) + ");";

            System.out.println("Table Creation SQL for " + className + " is:\n" + tableCreate);
        }
    }

    private static String getConstraints(Constraints con) {
        String constraints = "";
        if (!con.allowNull()) {
            constraints += " NOT NULL";
        }
        if (con.primaryKey()) {
            constraints += " PRIMARY KEY";
        }
        if (con.unique()) {
            constraints += "UNIQUE";
        }
        return constraints;
    }
}

/*
output:
Table Creation SQL for Member is:
CREATE TABLE member(
FIRSTNAME VARCHAR(30),
LASTNAME VARCHAR(50),
AGE INT,
REFERENCE VARCHAR(30) PRIMARY KEY);
 */
