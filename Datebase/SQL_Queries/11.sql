SELECT datediff(curdate(), (select date from messages where id = (select min(id) from messages)));