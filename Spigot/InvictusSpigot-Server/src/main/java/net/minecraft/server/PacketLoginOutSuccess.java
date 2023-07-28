package net.minecraft.server;

import com.eatthepath.uuid.FastUUID;
import com.mojang.authlib.GameProfile;

import java.io.IOException;
import java.util.UUID;

public class PacketLoginOutSuccess implements Packet<PacketLoginOutListener> {

    private GameProfile a;

    public PacketLoginOutSuccess() {
    }

    public PacketLoginOutSuccess(GameProfile gameprofile) {
        this.a = gameprofile;
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = new GameProfile(FastUUID.parseUUID(packetdataserializer.c(36)), packetdataserializer.c(16));
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        UUID uuid = this.a.getId();

        packetdataserializer.a(uuid == null ? "" : FastUUID.toString(uuid));
        packetdataserializer.a(this.a.getName());
    }

    public void a(PacketLoginOutListener packetloginoutlistener) {
        packetloginoutlistener.a(this);
    }

}
