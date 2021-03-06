/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#if defined(USE_TI_XML) || defined(USE_TI_NETWORK)

#import "TiDOMNodeListProxy.h"
#import "TiDOMNodeProxy.h"
#import "TiUtils.h"

@implementation TiDOMNodeListProxy

-(void)dealloc
{
	[nodes release];
	[super dealloc];
}

-(NSString*)apiName
{
    return @"Ti.XML.NodeList";
}

-(void)setNodes:(NSArray*)nodes_
{
    if (nodes == nodes_) {
        return;
    }
    for (TiDOMNodeProxy *node in nodes) {
        if (![nodes_ containsObject:node]) {
            [self forgetProxy:node];
        }
    }
	[nodes release];
	nodes = [nodes_ retain];
    for (TiDOMNodeProxy *node in nodes) {
        [[self pageContext] registerProxy:node];
        [self rememberProxy:node];
    }
}

-(id)item:(id)args
{
	ENSURE_SINGLE_ARG(args,NSObject);
	int index = [TiUtils intValue:args];
    
	if ( (index < [nodes count]) && (index >=0) )
	{
		return [nodes objectAtIndex:index];
	}
	return [NSNull null];
}

-(NSNumber*)length
{
    return NUMUINTEGER([nodes count]);
}



@end

#endif