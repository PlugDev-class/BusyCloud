package de.plugdev.cloud.external.permissions.netlistener;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.permissions.utils.PermissionsGroup;
import de.terrarier.netlistening.api.DataContainer;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodePermissionsListener implements DecodeListener {

	@Override
	public void trigger(DecodeEvent event) {
		String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Permissions")) {
			String cloudKey = event.getData().read();
			String command = event.getData().read();
			switch (command.toLowerCase()) {
			case "requestgroups": {
				for (PermissionsGroup group : ApplicationInterface.getAPI().getPermissionsSystem().getGroups()) {
					DataContainer container = new DataContainer();
					container.add("returnrequestedgroup");
					container.add(group.getGroupId());
					container.add(group.getGroupName());
					container.add(group.getGroupPrefix());
					container.add(group.getGroupSuffix());
					container.add(group.getGroupHeight());
					for (String permission : group.getPermissions()) {
						container.add(permission);
					}
					if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByKey(cloudKey) != null) {
						ApplicationInterface.getAPI().getInfrastructure().getSpigotServerByKey(cloudKey).getConnection()
								.sendData(container);
					}
					if (ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(cloudKey) != null) {
						ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(cloudKey).getConnection()
								.sendData(container);
					}
				}
				break;
			}
			default:
				break;
			}
		}
	}

}
