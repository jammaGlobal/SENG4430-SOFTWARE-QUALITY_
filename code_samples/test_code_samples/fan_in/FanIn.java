public class FanIn1 {
    public FanIn1() {}

    public void test1() {
    }
}

public class FanIn2 {
    public void test2() {
        FanIn1 f = new FanIn1();
        f.test1();
    }
}

//public class FanIn3 {
//    public FanIn3() {}
//
//    public void test3() {
//        FanIn1 f = new FanIn1();
//        f.test1();
//    }
//}
//
//public class FanOut4 {
//    public void test4() {
//        FanIn3 f = new FanIn3();
//        f.test3();
//    }
//}