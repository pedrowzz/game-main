package com.minecraft.core.util.skin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkinCategory {

    DEFAULT("Padrão", "Confira as skins padrão do servidor.", "c52fb388e33212a2478b5e15a96f27aca6c62ac719e1e5f87a1cf0de7b15e918"),
    ANIMES("Animes", "Reviva as animações da cultura japonesa.", "d640ae466162a47d3ee33c4076df1cab96f11860f07edb1f0832c525a9e33323"), //
    MOVIES("Filmes e Séries", "Torne-se um personagem do universo cinematográfico.", "463ad8cbf68a842c341c9ccc3433b443f03f77b0de2648bcc93c4c47a806d"), //
    HEROES("Heróis", "Brinque de ser um super-herói.", "a2b7144db3a9fd88c3797dcf98b46eb5381b9d5fda77e348157037177ac88e"), //
    GAMES("Jogos", "Inspire-se em seu personagem favorito dos games.", "af50fc40cbffeaaa8dcc1274c18671b552e73648f6b2336e13057606141d47f"), //
    MINECRAFT("Minecraft", "Baseie-se em nossas raízes.", "219e36a87baf0ac76314352f59a7f63bdb3f4c86bd9bba6927772c01d4d1"), //
    YOUTUBERS("Youtubers", "Jogue como seu youtuber favorito.", "d2f6c07a326def984e72f772ed645449f5ec96c6ca256499b5d2b84a8dce");

    private final String name, description, url;

    @Getter
    public static final SkinCategory[] values;

    static {
        values = values();
    }

}