package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    //orderId , Order   ordersDb
    HashMap<String,Order> hm1 = new HashMap<>();

    //partnerId, DeliveryPartner     deliveryPartnersDb
    HashMap<String,DeliveryPartner> hm2 = new HashMap<>();

    //partnerId, Order List    partnerOrdersDb
    HashMap<String,List<String>> hm3 = new HashMap<>();

    //unassigned-orders
    //orderId, partnerId      orderPartnerDb
    HashMap<String,String> hm4 = new HashMap<>();

    public void addOrder(Order order) {
        hm1.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        hm2.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(hm1.containsKey(orderId) && hm2.containsKey(partnerId)) {
            hm4.put(orderId, partnerId);

            List<String> list = new ArrayList<>();
            if(hm3.containsKey(partnerId))
                list = hm3.get(partnerId);
            list.add(orderId);

            hm3.put(partnerId,list);

            DeliveryPartner deliveryPartner = hm2.get(partnerId);
            deliveryPartner.setNumberOfOrders(list.size());
        }
    }

    public Order getOrderById(String orderId) {
        return hm1.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return hm2.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return hm3.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return hm3.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> list1 = new ArrayList<>();
        for(String orderId : hm1.keySet())
        {
            list1.add(orderId);
        }
        return list1;
    }

    public Integer getCountOfUnassignedOrders() {
        return hm1.size() - hm4.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int count = 0;
        List<String> order = hm3.get(partnerId);

        for(String orderId : order)
        {
            int currTime = hm1.get(orderId).getDeliveryTime();
            if(currTime > time) count++;
        }

        return count;
    }

    public void deletePartnerById(String partnerId) {
        hm2.remove(partnerId);

        List<String> list1 = hm3.get(partnerId);
        hm3.remove(partnerId);
        for(String orderId : list1)
        {
            hm4.remove(orderId);
        }
    }

    public void deleteOrderById(String orderId) {
        hm1.remove(orderId);

        String partnerId = hm4.get(orderId);
        hm4.remove(orderId);

        hm3.get(partnerId).remove(orderId);

        hm2.get(partnerId).setNumberOfOrders(hm3.get(partnerId).size());
    }


    public int getLastDeliveryTimeByPartnerId(String partnerId) {

        int max = 0;
        List<String> order = hm3.get(partnerId);
        for(String orderId : order)
        {
            int deliveryTime = hm1.get(orderId).getDeliveryTime();
            max = Math.max(max,deliveryTime);
        }
        return max;
    }
}
