package com.minecraft.core.util.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum SkinSubcategory {

    POKEMON("Pokémon", "Veja as skins da série Pokémon.", SkinCategory.ANIMES, "d43d4b7ac24a1d650ddf73bd140f49fc12d2736fc14a8dc25c0f3f29d85f8f"),
    NARUTO("Naruto", "Veja as skins da série Naruto.", SkinCategory.ANIMES, "f853215e4e3946f2006dfbea61c923ce7342b13f23fa7f35f2a40e8453eac7e5"),
    AMONG_US("Among Us", "Veja as skins do jogo Among Us.", SkinCategory.GAMES, "de2863a6bf82cc14177c63392c4fdcdfde29007ca930b79c165d2f653ae5b577"),
    MARIO("Mario", "Veja as skins do jogo Mario Bross", SkinCategory.GAMES, "65dd1a64e529c90dd55d76733dfb58e3663a12472fb3c7657e2661ccf172fe64"),
    ROUND_6("Round 6", "Veja as skins da série Round 6", SkinCategory.MOVIES, "680553855203dd41642390f11a80fc22db0907f4db15694e9e1e90a092e88e62"),
    STAR_WARS("Star Wars", "Veja as skins dos filmes de Star Wars.", SkinCategory.MOVIES, "65b4375319cbd636e0b5646d4274292b304912b6e19903158b127aec37301ee"),
    AVENGERS("Avengers", "Veja as skins dos filmes de Avengers", SkinCategory.HEROES, "c13f1f464fdbc1f9364a626608ec7294c0be474a7f569fc7b28bc4a85ff0e520"),
    JUSTICE_LEAGUE("Justice League", "Veja as skins dos filmes de Justice League", SkinCategory.HEROES, "8bdaa3a86b97b06ad4cfd19012f63948a069df731a721de49b2701247afe5fc"),
    LEGENDS("Legends", "Veja as skins das lendas do Minecraft.", SkinCategory.MINECRAFT, "98ffc130fc19b7ec36eda4d4da0d364d1e27adf22bc54c4a9256b5addba3"),
    MOBS("Mobs", "Veja as skins dos monstros do Minecraft.", SkinCategory.MINECRAFT, "3e8e0c40b0e931c77c264eb0a050d7e6ebbe5abf3ea92ae147548e5d3ec7bd82"),
    MALES("Masculino", "Veja as skins do gênero masculino.", SkinCategory.DEFAULT, "24eb3ad5fe1e9c26e283df28e37a3b5b38097ee146140d6e12969b9f9a607389"),
    FEMALES("Feminino", "Veja as skins do gênero feminino.", SkinCategory.DEFAULT, "89432f544144cd1f9d2be94096289070d03c1705b1e8de232cc85363a7403a8d"),
    YOUTUBERS("Youtubers", "Veja as skins de alguns youtubers.", SkinCategory.YOUTUBERS, "39e4b8dd69b34df68f3069776a3fc68f84d5709f6338ade45fe9b5737ab8396"),
    YOUTUBERS_PLUS("Youtubers+", "Veja as skins de nossos youtubers+.", SkinCategory.YOUTUBERS, "973fc0ce1726064cf8973590de22a430a982d720b99077391fdb06d3dc909f49");

    private final String name, description;
    private final SkinCategory category;
    private final String url;

    @Getter
    public static final SkinSubcategory[] values;

    static {
        values = values();
    }

    public static SkinSubcategory getSkinSubCategory(final String name) {
        return Arrays.stream(getValues()).filter(skinSubcategory -> skinSubcategory.getName().contains(name)).findFirst().orElse(null);
    }

    public static List<SkinSubcategory> getSkinSubcategories(final SkinCategory skinCategory) {
        return Arrays.stream(getValues()).filter(subcategory -> subcategory.getCategory() == skinCategory).collect(Collectors.toList());
    }

}