package OOP.Solution;

import java.util.*;
import java.util.stream.Collectors;
import OOP.Provided.*;


public class UserImpl implements User {
    private int userID;
    private String userName;
    private int userAge;
    private HashMap<Song, Integer> ratedSongs;
    private Set<User> friends;

    public UserImpl(int userID, String userName, int userAge) {
        this.userID = userID;
        this.userName = userName;
        this.userAge = userAge;
        this.ratedSongs = new HashMap<>();
        this.friends = new HashSet<>();
    }

    @Override
    public int getID() {
        return userID;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public int getAge() {
        return userAge;
    }

    @Override
    public User rateSong(Song song, int rate) throws IllegalRateValue, SongAlreadyRated {
        if (rate < 0 || rate > 10)
            throw new IllegalRateValue();

        if (ratedSongs.containsKey(song))
            throw new SongAlreadyRated();

        ratedSongs.put(song, rate);

        return this;
    }

    @Override
    public double getAverageRating() {
        if (ratedSongs.isEmpty())
            return 0;

        double sum = 0;

        for (int rating : ratedSongs.values())
            sum += rating;

        return sum / ratedSongs.size();
    }

    @Override
    public int getPlaylistLength() {
        int length = 0;

        for (Song song : ratedSongs.keySet())
            length += song.getLength();

        return length;
    }

    @Override
    public Collection<Song> getRatedSongs() {
        return ratedSongs.keySet().stream()
                .sorted(Comparator.comparingInt((Song s) -> ratedSongs.get(s)).reversed()
                        .thenComparingInt(Song::getLength)
                        .thenComparingInt(Song::getID).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Song> getFavoriteSongs() {
        return ratedSongs.keySet().stream()
                .filter(song -> ratedSongs.get(song) >= 8)
                .sorted(Comparator.comparingInt(Song::getID))
                .collect(Collectors.toList());
    }

    @Override
    public User AddFriend(User user) throws SamePerson, AlreadyFriends {
        if (this == user)
            throw new SamePerson();

        if (friends.contains(user))
            throw new AlreadyFriends();

        friends.add(user);

        return this;
    }

    @Override
    public boolean favoriteSongInCommon(User user) {
        Set<Song> thisFavoriteSongs = getFavoriteSongs().stream().collect(Collectors.toSet());
        Set<Song> userFavoriteSongs = user.getFavoriteSongs().stream().collect(Collectors.toSet());

        for (Song song : thisFavoriteSongs)
            if (userFavoriteSongs.contains(song))
                return true;

        return false;
    }

    @Override
    public Map<User, Integer> getFriends() {
        Map<User, Integer> friendsMap = new HashMap<>();

        for (User friend : friends)
            friendsMap.put(friend, friend.getRatedSongs().size());

        return friendsMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserImpl)) return false;
        UserImpl user = (UserImpl) o;
        return userID == user.userID;
    }

    @Override
    public int compareTo(User other) {
        return Integer.compare(this.userID, other.getID());
    }
}