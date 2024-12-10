package OOP.Solution;

import java.util.*;
import java.util.stream.Collectors;
import OOP.Provided.*;


public class SongImpl implements Song {
    private int songID;
    private String songName;
    private int length;
    private String singerName;
    private HashMap<User, Integer> raters;
    private HashMap<Integer, Set<User>> ratings;

    public SongImpl(int songID, String songName, int length, String singerName) {
        this.songID = songID;
        this.songName = songName;
        this.length = length;
        this.singerName = singerName;
        this.raters = new HashMap<>();
        this.ratings = new HashMap<>();
    }

    @Override
    public int getID() {
        return songID;
    }

    @Override
    public String getName() {
        return songName;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String getSingerName() {
        return singerName;
    }

    @Override
    public void rateSong(User user, int rate) throws User.IllegalRateValue, User.SongAlreadyRated {
        if (rate < 0 || rate > 10)
            throw new User.IllegalRateValue();

        if (raters.containsKey(user))
            throw new User.SongAlreadyRated();

        raters.put(user, rate);

        if (!ratings.containsKey(rate))
            ratings.put(rate, new HashSet<>());
        ratings.get(rate).add(user);
    }

    @Override
    public Collection<User> getRaters() {
        return raters.keySet().stream()
                .sorted(Comparator.comparingInt((User u) -> raters.get(u)).reversed()
                        .thenComparingInt(User::getAge)
                        .thenComparingInt(User::getID).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Set<User>> getRatings() {
        return ratings;
    }

    @Override
    public double getAverageRating() {
        if (raters.isEmpty())
            return 0;

        double sum = 0;

        for (int rating : raters.values())
            sum += rating;

        return sum / raters.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongImpl)) return false;
        SongImpl song = (SongImpl) o;
        return getID() == song.getID();
    }

    @Override
    public int compareTo(Song o) {
        return Integer.compare(this.getID(), o.getID());
    }

}
