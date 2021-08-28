package com.thoughtworks.codepairing.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingCart {
    private List<Product> products;
    private Customer customer;

    public ShoppingCart(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Order checkout() {
        double totalPrice = 0;
        int countBuy2Get1 = 0;
        int countDisProduct = 0;
        double Buy2Get1Price=0;
        int loyaltyPointsEarned = 0;
        Map<Product,Integer> map = new HashMap<>();

        for (Product product : products) {
            double discount = 0;
            if (product.getProductCode().startsWith("DIS_10")) {
                discount = (product.getPrice() * 0.1);
                loyaltyPointsEarned += (product.getPrice() / 10);
            } else if (product.getProductCode().startsWith("DIS_15")) {
                discount = (product.getPrice() * 0.15);
                loyaltyPointsEarned += (product.getPrice() / 15);
            }
            else if( product.getProductCode().startsWith("DIS_20")) {
                discount = (product.getPrice()*0.20);
                loyaltyPointsEarned+=(product.getPrice()/20);
            }
            else if( product.getProductCode().startsWith("BULK_BUY_2_GET_1")){
                map.put(product,map.getOrDefault(product,0)+1);

            }
            else {
                loyaltyPointsEarned += (product.getPrice() / 5);
            }

            totalPrice += product.getPrice() - discount;

        }
        for(Map.Entry<Product,Integer> entry : map.entrySet()){
            int count=entry.getValue();
            int countThisProduct=count/3;
            double thisPrice=(entry.getKey().getPrice())*(countThisProduct);
            totalPrice-= thisPrice;
        }
        return new Order(totalPrice, loyaltyPointsEarned);
    }

    @Override
    public String toString() {
        return "Customer: " + customer.getName() + "\n" + "Bought:  \n" + products.stream().map(p -> "- " + p.getName()+ ", "+p.getPrice()).collect(Collectors.joining("\n"));
    }
}
