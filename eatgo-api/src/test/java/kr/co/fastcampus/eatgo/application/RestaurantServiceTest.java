package kr.co.fastcampus.eatgo.application;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import kr.co.fastcampus.eatgo.domain.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class RestaurantServiceTest {

    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    public void setUP(){
        MockitoAnnotations.initMocks(this);
        mockRestaurantRepository();
        mockMenuItemRepository();
        restaurantService = new RestaurantService(restaurantRepository, menuItemRepository);

    }

    private void mockMenuItemRepository() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Kimchi"));
        given(menuItemRepository.findAllByRestaurantId(1004L)).willReturn(menuItems);
    }

    private void mockRestaurantRepository() {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = new Restaurant(1004L, "Bob zip", "Seoul");
        restaurants.add(restaurant);
        given(restaurantRepository.findAll()).willReturn(restaurants);
        given(restaurantRepository.findByid(1004L)).willReturn(java.util.Optional.of(restaurant));
    }

    @Test
    public void getRestaurant(){
        Restaurant restaurant = restaurantService.getRestaurant(1004L);
        assertThat(restaurant.getId(), is(1004L));
        MenuItem menuItem = restaurant.getMenuItems().get(0);
        assertThat(menuItem.getName(), is("Kimchi"));
    }

    @Test
    public void getRestaurants(){
        List<Restaurant> restaurants = restaurantService.getRestaurants();
        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getId(), is(1004L));
    }

    @Test
    public void addRestaurant(){
        Restaurant restaurant = new Restaurant("BeRyong", "Busan");
        Restaurant saved = new Restaurant(1234L,"BeRyong","Busan");
        given(restaurantRepository.save(any())).willReturn(saved);
        Restaurant created = restaurantService.addRestaurant(restaurant);
        assertThat(created.getId(), is(1234L));
    }

    @Test
    public void updateRestaurant(){
        Restaurant restaurant = new Restaurant(1004L, "Bob zip","Seoul");

        given(restaurantRepository.findByid(1004L)).willReturn(Optional.of(restaurant));

        restaurantService.updateRestaurant(1004L,"Sool zip","Busan");

        assertThat(restaurant.getName(), is("Sool zip"));
    }
}