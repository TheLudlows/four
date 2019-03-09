package io.four.invoke;

public class UserServiceImpl1 implements UserService1 {
    @Override
    public String getName(String hello) {
       return "four:"+hello;
    }

    @Override
    public int getAge() {
      return 1;
    }
}
