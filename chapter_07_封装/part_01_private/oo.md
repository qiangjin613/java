1. 不能因为类中某个对象的引用是 private，就认为其他对象也无法拥有该对象的 public 引用。
比如这里通过 makeAS() 访问到了 private S() 构造器，又比如通过 getter/setter 方法对 private 属性进行操作。
```java
class S {
    // 通过 private 控制如何创建对象，防止同包的其他类直接访问这个构造器
    private S() {}
    // 创建 S 对象，必须调用 makeAS() 方法来创建对象
    static S makeAS() {
        return new S();
    }
}
```

2. private 与 final

3. private 与 内部类
