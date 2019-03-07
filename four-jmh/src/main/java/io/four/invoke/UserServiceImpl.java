package io.four.invoke;

public class UserServiceImpl implements UserService {
    @Override
    public String getName(String hello) {
        return hello + ":four";
    }

    @Override
    public int getAge() {
        return 1111;
    }
}
