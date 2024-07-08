package practice01;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Inventory {
    private Product[] products; //제품 목록 배열
    private int size; // 현재 제품 수
    public Inventory() {
        products = new Product[100]; // 초기 크기 100의 배열 생성
        size = 0;
        increaseProduct(new Product("빵",  2000, "2024-12-31", false, 5));
        increaseProduct(new Product("우유", 3000, "2024-06-12", false, 5));
        increaseProduct(new Product("두부", 1500, "2024-06-12", false, 5));
        increaseProduct(new Product("오뎅", 1000, "2024-06-07", false, 5));
        increaseProduct(new Product("과자", 1500, "2024-12-31", false, 5));
        increaseProduct(new Product("라면", 1000, "2024-12-31", false, 5));
        increaseProduct(new Product("달걀", 4000, "2024-12-31", false, 5));
        increaseProduct(new Product("담배", 4500, "2024-12-31", true, 8));
        increaseProduct(new Product("술",  3000, "2024-12-31", true, 8));
        increaseProduct(new Product("김밥", 3000, "2024-12-31", false, 5));
    }
    public void increaseProduct(Product product) { // 제품 추가 및 기존 제품 수량 증가
        for (int i = 0; i < size; i++) {
            if (products[i].name.equals(product.name)) {
                products[i].quantity += product.quantity; // 기존 제품의 수량 증가
                return;
            }
        }
        if (size >= products.length) { //배열이 가득 찬 경우
            resize(); //배열 크기를 2배로 늘림
        }
        products[size] = product; // 새로운 제품 추가기능
        size++;
    }

    public void removeProduct(String name, int quantity) { // 제품 수량 지정된 수량만큼 제거
        for (int i = 0; i < size; i++) {
            if (products[i].name.equals(name)) {
                if (products[i].quantity >= quantity) {
                    products[i].quantity -= quantity;
                    System.out.println(name + " " + quantity + "개 제거됨");
                    return;
                } else {
                    System.out.println("재고가 부족합니다.");
                    return;
                }
            }
        }
        System.out.println(name + " 없음");
    }

    public Product findProduct(String name) { // 제품을 이름으로 검색
        for (int i = 0; i < size; i++) {
            if (products[i].name.equals(name)) {
                return products[i];
            }
        }
        return null;
    }

    public Product[] getProducts() { // 현재 모든 제품 목록을 반환
        Product[] currentProducts = new Product[size];
        for (int i = 0; i < size; i++) {
            currentProducts[i] = products[i];
        }
        return currentProducts;
    }

    public void checkExpiry() { // 모든 제품의 유통기한 확인
        System.out.println("유통기한 확인:");
        for (int i = 0; i < size; i++) {
            Product product = products[i];  
            System.out.println(product.name + ": " + product.getRemainingTime());
            }
        }

    private void resize() { //배열의 크기를 2배로 늘림
        Product[] newProducts = new Product[products.length * 2]; // 크기를 2배로 늘린 새로운 배열 생성
        System.arraycopy(products, 0, newProducts, 0, products.length); // 기존 배열의 요소를 새로운 배열로 복사
        products = newProducts; // 새로운 배열로 교체
    }
}