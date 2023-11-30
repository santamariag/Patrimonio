package eu.tasgroup.kafka.util;

public interface KeyExtractor<TKey, TEvent> 
{
	TKey extractKey(TEvent event);
}
