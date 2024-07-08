package practice01;

public class CashRegister {
    int balance; // 현재 잔고
    int totalbalance; // 총 매출액
    int wagePerMinute = 9800; //분당 임금 설정

    public CashRegister(int alltotalBalance) { //초기 잔고 설정
    	 this.balance = alltotalBalance;
         this.totalbalance = alltotalBalance;
    }

    public void addSales(int amount) { // 판매 금액을 매출액에 추가
        totalbalance += amount;
    }

    public void calculateWage(int minutesWorked) {
        int wage = minutesWorked * wagePerMinute; //분당일한시간과 분당임금을 곱해서 하루 임금을 설정
        System.out.println("오늘의 임금: " + wage + "원");
    }

    public int getBalance() {
        return totalbalance;
    }

	public int getTotalSales() { 
		// TODO Auto-generated method stub
		return totalbalance-balance;
	}

	public int processCashPayment(int cash, int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void processCardRefund(int i) {
		// TODO Auto-generated method stub
		
	}

	public void processCashRefund(int i) {
		// TODO Auto-generated method stub
		
	}
}