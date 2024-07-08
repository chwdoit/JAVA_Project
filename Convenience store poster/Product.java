package practice01;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    String name; // 제품 이름
    int price; // 제품 가격
    String expireDate; // 제품 유통기한
    boolean isRestricted; // 성인전용 제한 여부
	public int quantity; // 제품 수량

    public Product(String name, int price, String expireDate, boolean isRestricted, int quantity) { // 제품의 모든 속성 초기화
        this.name = name;
        this.price = price;
        this.expireDate = expireDate;
        this.isRestricted = isRestricted;
        this.quantity = quantity;
    }
    public void getMaskedQuantity(int quantity) { // 제품 수량을 *표시로 표현
        for(int i = 0; i < quantity; i++) {
        	System.out.print("*");
        }
        System.out.println();
    }
    public long getRemainingDays() { // 유통기한까지 남은 시간을 계산하여 문자열로 변환
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // 날짜 형식 지정
            Date expiry = sdf.parse(this.expireDate);// 유통기한 문자열을 Date 객체로 변환
            Date now = new Date(); // 현재 날짜시간
            long remainingMillis = expiry.getTime() - now.getTime(); // 유통기한까지 남은 시간
            return remainingMillis / (1000 * 60 * 60 * 24); // 밀리초를 일 단위로 변환
        } catch (Exception e) {
            return -1; // 오류 발생 시 -1 반환
        }
    }
    public String getRemainingTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expiry = sdf.parse(this.expireDate);
            Date now = new Date();
            long remainingMillis = expiry.getTime() - now.getTime();
            if (remainingMillis <= 0) {
                return "유통기한이 지났습니다"; // 유통기한이 지나면 
            } else {
                long days = remainingMillis / (1000 * 60 * 60 * 24); // 밀리초를 일 단위로 변환
                long hours = (remainingMillis / (1000 * 60 * 60)) % 24; // 일 단위로 나누고 남은 시간을 시간단위로 변환
                long minutes = (remainingMillis / (1000 * 60)) % 60; // 시간 단위로 나누고 남은 시간을 분 단위로 변환
                return String.format("%d일 %d시간 %d분 남았습니다", days, hours, minutes); // 남은 시간을 문자열로 변환
            }
        } catch (Exception e) {
            return "유통기한 형식 오류"; // 날짜 형식이 잘못된 경우에 출력
        }
    }
	public Object getName() {
		// TODO Auto-generated method stub
		return name;
	}
	public boolean isRestricted() {
		// TODO Auto-generated method stub
		return isRestricted;
	}
	public Object getExpireDate() {
		// TODO Auto-generated method stub
		return expireDate;
	}
	public Object getPrice() {
		// TODO Auto-generated method stub
		return price;
	}
}