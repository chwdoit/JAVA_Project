package practice01;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Store {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 사용자 입력을 받기위해 Scanner 객체 생성
        Inventory inventory = new Inventory(); // 재고 관리를 위한 객체 생성
        CashRegister register = new CashRegister(390500); // 현금 관리를 위한 객체 생성
        User user = new User("alba", "password");// 처음 아이디 비번을 객체 생성

     // 로그인 절차 추가
        while (true) {
            System.out.println("아이디를 입력하세요:");
            String inputUsername = sc.next();
            System.out.println("비밀번호를 입력하세요:");
            String inputPassword = sc.next();

            if (user.login(inputUsername, inputPassword)) {
                System.out.println("로그인되었습니다.");
                break;
            } else {
                System.out.println("아이디 또는 비밀번호가 잘못되었습니다. 다시 시도하세요.");
            }
        }
        while (true) {  	
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("1. 재고 체크 | 2. 현재 잔고 체크 | 3. 매출액 | 4. 유통기한 체크 | 5. 업무 시작 | 6. 종료");
            System.out.println("-------------------------------------------------------------------------");
            int choice = sc.nextInt(); // 사용자로부터 입력받기
            if (choice == 1) { 
                System.out.println("재고 목록:");
                for (Product product : inventory.getProducts()) {
                    if (product.quantity != 0) { // 수량이 0이 아니면 속성 출력
                        System.out.printf("%s\t- %d원\t - %s - %s- ", product.getName(), product.getPrice(), product.getExpireDate(), (product.isRestricted() ? "성인" : "일반"));
                        product.getMaskedQuantity(product.quantity); // 수량을 미스킹으로 표현
                    }
                }
            } else if (choice == 2) {
                System.out.println("현재 잔고: " + register.getBalance() + "원"); // 현재 잔고 출력 
            } else if (choice == 3) {
                System.out.println("매출액: " + register.getTotalSales() + "원"); // 오늘 판매 금액으로 번 금액만 출력되도록 함
            } else if (choice == 4) {
                inventory.checkExpiry(); // 유통기한 확인 후 출력
            } else if (choice == 5) {
                System.out.println("---------------------------------------");
                System.out.println("1. 물건 판매 | 2. 입고 | 3. 환불 | 4. 물건 검색");
                System.out.println("---------------------------------------");
                int subChoice = sc.nextInt(); // 판매할 수량을 입력받음
                if (subChoice == 1) {
                    while (true) {
                        System.out.println("판매할 물품명 (종료하려면 '종료' 입력):");
                        String productName = sc.next();
                        if (productName.equals("종료")) break; //종료를 입력하면 창이 처음으로 넘어감 
                        Product product = inventory.findProduct(productName); //판매할 제품을 입력
                        if (product != null) { 
                            System.out.println("수량:");
                            int quantity = sc.nextInt(); //판매할 수량을 입력
                            if (product.isRestricted) { //성인 확인이 필요한 제품일때 생년월일 입력 미성년자시 구매할수 없는 창으로 뜨며 처음 창으로 넘어감
                                System.out.println("생년월일을 입력하세요 (YYYYMMDD):");
                                String birthdate = sc.next();
                                if (!isAdult(birthdate)) {
                                    System.out.println("미성년자는 구매할 수 없습니다.");
                                    continue;
                                }
                            }
                            System.out.println("가격: " + product.price * quantity);
                            System.out.println("결제 방식 (1: 카드 2: 현금):");
                            int paymentMethod = sc.nextInt();
                            if (paymentMethod == 1) {
                                System.out.println("카드번호 입력 (XXXX-XXXX-XXXXXXXX):"); //카드 선택시 카드 번호를 입력
                                String cardNumber = sc.next();
                                if (cardNumber.matches("\\d{4}-\\d{4}-\\d{8}")) {
                                    register.addSales(product.price * quantity); // 매출액 추가
                                    inventory.removeProduct(product.name, quantity); // 재고에서 제품 제거
                                    System.out.println("결제가 완료되었습니다.");
                                } else {
                                    System.out.println("유효하지 않은 카드번호입니다."); //카드 번호가 자릿수가 모자라거나 오버될시 이렇게 창이 뜨도록 설정
                                }
                            } else if (paymentMethod == 2) {
                                register.addSales(product.price * quantity); // 매출액 추가
                                inventory.removeProduct(product.name, quantity); // 재고에서 제품제거
                                System.out.println("결제가 완료되었습니다."); 
                            } else {
                                System.out.println("유효하지 않은 결제 방식입니다.");
                            }
                        } else {
                            System.out.println("재고에 없는 제품입니다."); // 재고가 없으면 이 창이 뜨도록 하며 바로 처음창으로 넘어감
                        }
                    }
                } else if (subChoice == 2) {
                    System.out.println("입고할 물품명:");
                    String productName = sc.next();
                    System.out.println("가격:");
                    int price = sc.nextInt();
                    System.out.println("유통기한 (YYYY-MM-DD):");
                    String expireDate = sc.next();
                    System.out.println("성인 확인 제품인가요? (1: 예 2: 아니오):");
                    boolean isRestricted = sc.nextInt() == 1;
                    System.out.println("수량:");
                    int quantity = sc.nextInt();
                    Product product = new Product(productName, price, expireDate, isRestricted, quantity); //새로운 제품 생성 처음 설정한 10개의 항목이 아나여도 입력시 재고추가됨
                    inventory.increaseProduct(product); // 재고에 제품 추가
                    System.out.println("제품이 입고되었습니다.");
                } else if (subChoice == 3) {
                    while (true) {
                        System.out.println("환불할 물품명 (종료하려면 '종료' 입력):");
                        String productName = sc.next();
                        if (productName.equals("종료")) break;
                        Product product = inventory.findProduct(productName); // 환불할 제품 검색
                        if (product != null) {
                            System.out.println("수량:");
                            int quantity = sc.nextInt(); // 수량 입력
                            register.addSales(-product.price * quantity); // 매출액에서 자동으로 차감되도록 함
                            inventory.increaseProduct(new Product(productName, product.price, product.expireDate, product.isRestricted, quantity));
                            System.out.println("환불이 완료되었습니다.");
                        } else {
                            System.out.println("환불할 제품이 재고에 없습니다.");
                        }
                    }
                } else if (subChoice == 4) {
                    System.out.println("찾고자 하는 물건을 검색:");
                    String productName = sc.next();
                    Product product = inventory.findProduct(productName); // 재품 검색
                    if (product != null) {
                        System.out.println("이름: " + product.name);
                        System.out.println("유통기한: " + product.expireDate);
                        System.out.println("남은 수량: " + product.quantity);
                        System.out.println("가격: " + product.price);
                    }
                }
            } else if (choice == 6) {
                user.logout(); // 로그아웃
                Date logoutTime = new Date(); // 로그아웃 시간 기록
                long workTimeInMillis = logoutTime.getTime() - user.loginTime.getTime(); //일한 시간 계산
                int workMinutes = (int) (workTimeInMillis / (1000 * 60)); // 
                System.out.println("총 매출액: " + register.getTotalSales() + "원"); //오늘의 매출 얼마인지 확인
                register.calculateWage(workMinutes);
                System.out.println("로그인 시간: " + new SimpleDateFormat("HH:mm:ss").format(user.loginTime));
                System.out.println("로그아웃 시간: " + new SimpleDateFormat("HH:mm:ss").format(user.logoutTime));
                break;
            }
        }
        sc.close();
    }
    private static boolean isAdult(String birthdate) { // main 메서드 내에서 직접 호출되서 사용되므로 같은 클래스안에 집어넣음
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 생년월일 문자열을 날짜 형식으로 변환하기 위한 SimpleDateFormat 객체 생성
            Date birthDate = sdf.parse(birthdate); // 입력받은 생년월일 문자열을 Date 객체로 변환
            Date today = new Date(); // 현재 날짜를 Date 객체로 생성
            long ageInMillis = today.getTime() - birthDate.getTime(); // 현재 날짜와 생년월일의 차이를 밀리초 단위로 계산
            long age = ageInMillis / (1000L * 60 * 60 * 24 * 365); // 나이를 계산 (밀리초를 년 단위로 변환)
            return age >= 19; 
        } catch (Exception e) {
            return false; // 날짜 형식이 잘못되었거나 변환 중 오류가 발생한 경우 false 반환
        }
    }
}