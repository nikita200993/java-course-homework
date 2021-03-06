package ru.otus.vcs.newversion.objects;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.vcs.newversion.gitrepo.CommitMessage;
import ru.otus.vcs.newversion.path.VCSFileName;
import ru.otus.vcs.newversion.ref.Sha1;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class SerializationTest {

    @Test
    void testBlobSerialization() {
        final var blob = new Blob("Привет!".getBytes(StandardCharsets.UTF_8));
        Assertions.assertThat(GitObject.deserialize(blob.serialize()))
                .isEqualTo(blob);
    }

    @Test
    void testTreeLeafSerialization() {
        final var leaf = new TreeLeaf(
                FileType.Regular,
                VCSFileName.create("hey"),
                Sha1.hash("a")
        );
        Assertions.assertThat(TreeLeaf.deserialize(leaf.serialize()))
                .isEqualTo(leaf);
    }

    @Test
    void testTreeSerialization1() {
        final var first = new TreeLeaf(
                FileType.Directory,
                VCSFileName.create("abc"),
                Sha1.hash("b")
        );
        final var second = new TreeLeaf(
                FileType.Regular,
                VCSFileName.create("zz"),
                Sha1.hash("a")
        );
        final var tree = new Tree(List.of(first, second));
        Assertions.assertThat(GitObject.deserialize(tree.serialize()))
                .isEqualTo(tree);
    }

    @Test
    void testTreeSerialization2() {
        final var first = new TreeLeaf(
                FileType.Directory,
                VCSFileName.create("abc"),
                Sha1.hash("b")
        );
        final var second = new TreeLeaf(
                FileType.Regular,
                VCSFileName.create("zz"),
                Sha1.hash("a")
        );
        final var third = new TreeLeaf(
                FileType.Regular,
                VCSFileName.create("zzf"),
                Sha1.hash("add")
        );
        final var tree = new Tree(List.of(first, second, third));
        Assertions.assertThat(GitObject.deserialize(tree.serialize()))
                .isEqualTo(tree);
    }

    @Test
    void testCommitSerialization() {
        final var commit = new Commit(Sha1.hash("a"), Sha1.hash("b"), null, "adsa", CommitMessage.create("fasdf"));

        Assertions.assertThat(GitObject.deserialize(commit.serialize()))
                .isEqualTo(commit);
    }

    @Test
    void testCommitSerializationNoBlankLine() {
        final var dataWithNoBlankLine = "tree " + Sha1.hash("a") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "author vasya\n"
                + "message\n";

        Assertions.assertThatThrownBy(
                () -> Commit.deserialize(dataWithNoBlankLine.getBytes(StandardCharsets.UTF_8))
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void testCommitSerializationMoreThanTwoParents() {
        final var manyParents = "tree " + Sha1.hash("a") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "author vasya\n"
                + "\n"
                + "message\n";

        Assertions.assertThatThrownBy(
                () -> Commit.deserialize(manyParents.getBytes(StandardCharsets.UTF_8))
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void testCommitSerializationNoTree() {
        final var noTree = "parent " + Sha1.hash("b") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "author vasya\n"
                + "\n"
                + "message\n";

        Assertions.assertThatThrownBy(
                () -> Commit.deserialize(noTree.getBytes(StandardCharsets.UTF_8))
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void testCommitSerializationNoAuthor() {
        final var noTree = "tree " + Sha1.hash("a") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "parent " + Sha1.hash("b") + "\n"
                + "\n"
                + "message\n";
        Assertions.assertThatThrownBy(
                () -> Commit.deserialize(noTree.getBytes(StandardCharsets.UTF_8))
        ).isInstanceOf(IllegalStateException.class);
    }

}
