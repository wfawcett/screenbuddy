# --- !Ups
create fulltext index search_index on title (original_title);

# --- !Downs
drop index search_index on title;
