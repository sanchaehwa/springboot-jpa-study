package hellojpa;

public class ValueMain {

    public static void main(String[] args) {
        Integer a = 10;
        Integer b = a;
       // int b = a; //a가 복사가 되어 b
        //a =20; // a,b 다른 저장 공간을 갖고 있음 (=공유를 안하고 있음)
        System.out.println(a);
        System.out.println(b);

        Address address1 = new Address("Haeundae","Busan","23-1");
        Address address2 = new Address("Haeundae","Busan","23-1");
        //동일성
        System.out.println("address1 == address2" + (address1 == address2 ));
        //동등성
        System.out.println("address2.equals(address1) = " + address2.equals(address1));

    }
}
