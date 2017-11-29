package com.github.chen0040.jrl.ttt;

import org.testng.annotations.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BoardUnitTest {

    @Test
    public void testBoard() {
        Board board = new Board();

        for(int i=0; i < board.size(); ++i) {
            for(int j=0; j < board.size(); ++j) {
                assertThat(board.getCell(new Position(i, j))).isEqualTo(0);
            }
        }

        board.move(new Position(0, 0), 1);
        assertThat(board.getCell(new Position(0, 0))).isEqualTo(1);

        board.move(new Position(0, 1), 2);
        assertThat(board.getCell(new Position(0, 1))).isEqualTo(2);
    }
}
