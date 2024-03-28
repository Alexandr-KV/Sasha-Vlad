
public class Main {
    public static void main(String[] args) {
        int k = 0;
        int summa = 0;
        for (int i = 298435; i <= 363249; i++) {
            if (f(i)){
                k += 1;
                summa += i;
            }
        }
        System.out.println(k);
        System.out.println(summa/k);
    }

    static boolean f(int number){
        int k = 0;
        for (int i = 2; i< Math.sqrt(number)+1; i++){
            if (number % i == 0){
                if(i == number / i){
                    k += 1;
                }else{
                    k += 2;
                }
            }
            if (k > 2){
                return false;
            }
        }
        return k == 2;
    }
}