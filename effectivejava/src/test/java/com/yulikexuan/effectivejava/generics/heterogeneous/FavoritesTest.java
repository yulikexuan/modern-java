//: com.yulikexuan.effectivejava.generics.heterogeneous.FavoritesTest.java


package com.yulikexuan.effectivejava.generics.heterogeneous;


import com.google.common.collect.Maps;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Generic Heterogeneous Container - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FavoritesTest {

    private Favorites favorites;

    @BeforeEach
    void setUp() {
        this.favorites = Favorites.of(Maps.newHashMap());
    }

    @Test
    void test_Given_Different_Class_Literals_When_Save_Favorites_Then_Get_Them_Back() {

        // Given
        this.favorites.putFavorite(String.class, "Modern Java");
        this.favorites.putFavorite(Integer.class, 15);
        this.favorites.putFavorite(Class.class, Favorites.class);

        // When & Then
        assertThat(this.favorites.getFavorite(String.class))
                .isEqualTo("Modern Java");
        assertThat(this.favorites.getFavorite(Integer.class))
                .isEqualTo(15);
        assertThat(this.favorites.getFavorite(Class.class))
                .isEqualTo(Favorites.class);
    }

}///:~